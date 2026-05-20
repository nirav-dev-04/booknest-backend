package org.example.bookcart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.ApiResponse;
import org.example.bookcart.dto.AuthResponse;
import org.example.bookcart.dto.LoginRequest;
import org.example.bookcart.dto.RegisterRequest;
import org.example.bookcart.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // REGISTER API
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>>
    register(

            @Valid @RequestBody RegisterRequest request
    ) {

        String response =
                authService.register(request);

        return ResponseEntity.ok(

                ApiResponse.builder()

                        .success(true)

                        .message(response)

                        .data(null)

                        .build()
        );
    }

    // LOGIN API
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>>
    login(

            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(

                ApiResponse.<AuthResponse>builder()

                        .success(true)

                        .message("Login successful")

                        .data(response)

                        .build()
        );
    }
}