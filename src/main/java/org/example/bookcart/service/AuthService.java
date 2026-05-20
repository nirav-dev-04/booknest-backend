package org.example.bookcart.service;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.AuthResponse;
import org.example.bookcart.dto.LoginRequest;
import org.example.bookcart.dto.RegisterRequest;
import org.example.bookcart.entity.Cart;
import org.example.bookcart.entity.Role;
import org.example.bookcart.entity.User;
import org.example.bookcart.repository.CartRepository;
import org.example.bookcart.repository.UserRepository;
import org.example.bookcart.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    AuthService.class
            );

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final PasswordEncoder passwordEncoder;

    // REGISTER
    public String register(RegisterRequest request) {

        logger.info(
                "Register request received for email: {}",
                request.getEmail()
        );

        // CHECK EMAIL EXISTS
        if (userRepository.existsByEmail(
                request.getEmail()
        )) {

            logger.error(
                    "Email already exists: {}",
                    request.getEmail()
            );

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        // ENCRYPT PASSWORD
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.USER);

        // SAVE USER
        userRepository.save(user);

        // CREATE CART
        Cart cart = new Cart();

        cart.setUser(user);

        cartRepository.save(cart);

        logger.info(
                "User registered successfully: {}",
                user.getEmail()
        );

        return "User Registered Successfully";
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {

        logger.info(
                "Login request received for email: {}",
                request.getEmail()
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        boolean isPasswordMatched =
                passwordEncoder.matches(

                        request.getPassword(),

                        user.getPassword()
                );

        if (!isPasswordMatched) {

            logger.error(
                    "Invalid password attempt for email: {}",
                    request.getEmail()
            );

            throw new RuntimeException(
                    "Invalid Password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        logger.info(
                "User logged in successfully: {}",
                request.getEmail()
        );

        return new AuthResponse(

                token,

                user.getRole().name()
        );    }
}