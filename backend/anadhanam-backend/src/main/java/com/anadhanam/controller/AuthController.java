package com.anadhanam.controller;

import com.anadhanam.dto.RegisterRequest;
import com.anadhanam.model.User;
import com.anadhanam.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of(
                "message", "Registration successful!",
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole()
            )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail(
            IllegalArgumentException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            Map.of("error", exception.getMessage())
        );
    }
}

