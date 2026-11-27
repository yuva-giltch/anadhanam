package com.anadhanam.service;

import com.anadhanam.dto.RegisterRequest;
import com.anadhanam.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        if (users.containsKey(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setFullName(request.getFullName());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setOrganizationName(request.getOrganizationName());
        user.setPhoneNumber(request.getPhoneNumber());

        users.put(email, user);

        return user;
    }
}
