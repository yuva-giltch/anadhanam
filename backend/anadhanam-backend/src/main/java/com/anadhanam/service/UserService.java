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
    public User loginUser(String email, String password) {

    String normalizedEmail = email.toLowerCase().trim();

    User user = users.get(normalizedEmail);

    if (user == null ||
        !passwordEncoder.matches(password, user.getPasswordHash())) {
        throw new IllegalArgumentException("Invalid email or password");
    }

    return user;
}

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
