package org.example.bookcart.config;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // PASSWORD ENCODER

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION MANAGER

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    // SECURITY FILTER CHAIN

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // ENABLE CORS

                .cors(cors -> {})

                // DISABLE CSRF

                .csrf(csrf -> csrf.disable())

                // DISABLE HTTP BASIC

                .httpBasic(httpBasic ->
                        httpBasic.disable()
                )

                // STATELESS SESSION

                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // AUTHORIZATION RULES

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC APIs
                        // =========================

                        .requestMatchers(

                                "/auth/**",

                                "/swagger-ui/**",

                                "/v3/api-docs/**"

                        ).permitAll()

                        // =========================
                        // PUBLIC BOOK APIs
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/books/**"
                        ).permitAll()

                        // =========================
                        // ADMIN BOOK MANAGEMENT
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/books/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/books/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/books/**"
                        ).hasRole("ADMIN")

                        // =========================
                        // ADMIN DASHBOARD APIs
                        // =========================

                        .requestMatchers(
                                "/admin/**"
                        ).hasRole("ADMIN")

                        // =========================
                        // USER APIs
                        // =========================

                        .requestMatchers(

                                "/cart/**",

                                "/orders/**"

                        ).hasAnyRole("USER", "ADMIN")

                        // =========================
                        // OPTIONS REQUESTS
                        // =========================

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // =========================
                        // ANY OTHER REQUEST
                        // =========================

                        .anyRequest()

                        .authenticated()
                )

                // JWT FILTER

                .addFilterBefore(

                        jwtAuthFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}