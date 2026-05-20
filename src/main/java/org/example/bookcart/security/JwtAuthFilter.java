package org.example.bookcart.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.bookcart.entity.User;
import org.example.bookcart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    JwtAuthFilter.class
            );

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        logger.info("JWT filter running");

        final String authHeader =
                request.getHeader("Authorization");

        // CHECK HEADER
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            logger.warn("Authorization header missing or invalid");

            filterChain.doFilter(request, response);

            return;
        }

        String token = authHeader.substring(7);

        String email = null;

        // HANDLE INVALID TOKEN
        try {

            email = jwtService.extractEmail(token);

            logger.info("Email extracted from token");

        } catch (Exception e) {

            logger.error("Invalid JWT token");

            filterChain.doFilter(request, response);

            return;
        }

        // CHECK SECURITY CONTEXT
        if (email != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            // VALIDATE TOKEN
            if (user != null &&
                    jwtService.isTokenValid(
                            token,
                            user.getEmail()
                    )) {

                logger.info(
                        "JWT token validated successfully"
                );

                UserDetails userDetails =
                        new org.springframework.security.core.userdetails.User(

                                user.getEmail(),

                                user.getPassword(),

                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_" +
                                                        user.getRole().name()
                                        )
                                )
                        );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(

                                userDetails,

                                null,

                                userDetails.getAuthorities()
                        );

                authToken.setDetails(

                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);

                logger.info(
                        "Authentication set in security context"
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}