package com.anadhanam.service;

import com.anadhanam.dto.RegisterRequest;
import com.anadhanam.model.User;
import com.anadhanam.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setFullName(request.getFullName());
        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(request.getRole());
        user.setOrganizationName(request.getOrganizationName());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.saveUser(user);

        return user;
    }

    public User loginUser(String email, String password) {

        String normalizedEmail = email.toLowerCase().trim();

        User user = userRepository.findByEmail(normalizedEmail);

        if (user == null ||
                !passwordEncoder.matches(
                        password,
                        user.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return user;
    }
}
