package com.anadhanam.controller;

import com.anadhanam.dto.LoginRequest;
import com.anadhanam.dto.RegisterRequest;
import com.anadhanam.model.User;
import com.anadhanam.service.UserService;
import com.anadhanam.service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        try {
            User user = userService.registerUser(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                    "message", "Registration successful!",
                    "userId", user.getId(),
                    "email", user.getEmail(),
                    "role", user.getRole()
                )
            );

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("error", exception.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            User user = userService.loginUser(
                request.getEmail(),
                request.getPassword()
            );

            String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
            );

            return ResponseEntity.ok(
                Map.of(
                    "message", "Login successful!",
                    "userId", user.getId(),
                    "email", user.getEmail(),
                    "role", user.getRole(),
                    "token", token
                )
            );

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("error", "Invalid email or password")
            );
        }
    }
}
