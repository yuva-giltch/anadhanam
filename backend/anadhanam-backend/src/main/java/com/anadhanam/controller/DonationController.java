package com.anadhanam.controller;

import com.anadhanam.model.Donation;
import com.anadhanam.model.User;
import com.anadhanam.service.DonationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }
        @PostMapping("/ngo/{donationId}/claim")
    public ResponseEntity<?> claimDonation(
            @PathVariable String donationId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (!"NGO".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only NGOs can claim donations");
        }

        Donation claimed = donationService.claimDonation(
                donationId, user.getEmail());

        return ResponseEntity.ok(claimed);
    }
    @PostMapping("/ngo/{donationId}/assign-volunteer")
    public ResponseEntity<?> assignVolunteer(
            @PathVariable String donationId,
            @RequestBody java.util.Map<String, String> request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (!"NGO".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only NGOs can assign volunteers");
        }

        String volunteerEmail = request.get("volunteerEmail");

        if (volunteerEmail == null || volunteerEmail.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Volunteer email is required");
        }

        Donation assigned = donationService.assignVolunteer(
                donationId,
                user.getEmail(),
                volunteerEmail);

        return ResponseEntity.ok(assigned);

    }
        @GetMapping("/volunteer/assigned")
    public ResponseEntity<?> getAssignedDonations(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (!"VOLUNTEER".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only volunteers can access assigned donations");
        }

        return ResponseEntity.ok(
                donationService.getAssignedDonations(user.getEmail()));
    }

    @PutMapping("/volunteer/{donationId}/pickup-status")
    public ResponseEntity<?> updatePickupStatus(
            @PathVariable String donationId,
            @RequestBody java.util.Map<String, String> request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (!"VOLUNTEER".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only volunteers can update pickup status");
        }

        String pickupStatus = request.get("pickupStatus");

        if (pickupStatus == null || pickupStatus.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Pickup status is required");
        }

        Donation updated = donationService.updatePickupStatus(
                donationId,
                user.getEmail(),
                pickupStatus);

        return ResponseEntity.ok(updated);
    }
    @GetMapping("/ngo/available")
    public ResponseEntity<?> getAvailableDonations(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (!"NGO".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only NGOs can access available donations");
        }

        return ResponseEntity.ok(
                donationService.getAvailableDonations());
    }

    @PostMapping
    public ResponseEntity<?> createDonation(
            @RequestBody Donation donation,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (!"DONOR".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only donors can create donations");
        }

        Donation created = donationService.createDonation(
                donation, user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }

    @GetMapping("/{donationId}")
    public ResponseEntity<Donation> getDonationById(
            @PathVariable String donationId) {

        return ResponseEntity.ok(
                donationService.getDonationById(donationId));
    }

    @PutMapping("/{donationId}")
    public ResponseEntity<?> updateDonation(
            @PathVariable String donationId,
            @RequestBody Donation donation,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Donation updated = donationService.updateDonation(
                donationId, donation, user.getEmail());

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{donationId}")
    public ResponseEntity<?> deleteDonation(
            @PathVariable String donationId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        donationService.deleteDonation(
                donationId, user.getEmail());

        return ResponseEntity.ok("Donation deleted successfully");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(
            IllegalArgumentException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleForbidden(
            SecurityException exception) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }
        @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleConflict(
            IllegalStateException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }
}
