package com.anadhanam.controller;

import com.anadhanam.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Map<String, Object> profile = new LinkedHashMap<>();

        profile.put("id", user.getId());
        profile.put("fullName", user.getFullName());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());
        profile.put("organizationName", user.getOrganizationName());
        profile.put("phoneNumber", user.getPhoneNumber());

        return ResponseEntity.ok(profile);
    }
}
