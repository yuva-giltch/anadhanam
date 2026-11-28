package com.anadhanam.service;

import com.anadhanam.model.Donation;
import com.anadhanam.model.User;
import com.anadhanam.repository.DonationRepository;

import org.springframework.stereotype.Service;
import com.anadhanam.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    public DonationService(
        DonationRepository donationRepository,
        UserRepository userRepository) {

    this.donationRepository = donationRepository;
    this.userRepository = userRepository;
}

    public Donation createDonation(Donation donation, String donorEmail) {

        donation.setDonationId(UUID.randomUUID().toString());
        donation.setDonorEmail(donorEmail);
        donation.setStatus("AVAILABLE");
        donation.setPickupStatus("NOT_ASSIGNED");
        donation.setCreatedAt(Instant.now().toString());

        donationRepository.saveDonation(donation);

        return donation;
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAllDonations();
    }
    public List<Donation> getAvailableDonations() {

    return getAllDonations().stream()
            .filter(donation -> "AVAILABLE".equals(donation.getStatus()))
            .toList();
    }
        public Donation claimDonation(
            String donationId,
            String ngoEmail) {

        Donation donation = getDonationById(donationId);

        if (!"AVAILABLE".equals(donation.getStatus())) {
            throw new IllegalStateException(
                    "This donation is no longer available");
        }

        donation.setClaimedByNgoEmail(ngoEmail);
        donation.setClaimedAt(Instant.now().toString());
        donation.setStatus("CLAIMED_BY_NGO");
        donation.setPickupStatus("PENDING");

        donationRepository.saveDonation(donation);

        return donation;
    }

    public Donation assignVolunteer(
            String donationId,
            String ngoEmail,
            String volunteerEmail) {

        Donation donation = getDonationById(donationId);

        if (!ngoEmail.equals(donation.getClaimedByNgoEmail())) {
            throw new SecurityException(
                    "Only the NGO that claimed this donation can assign a volunteer");
        }

        if (!"CLAIMED_BY_NGO".equals(donation.getStatus())) {
            throw new IllegalStateException(
                    "Donation must be claimed before assigning a volunteer");
        }

        User volunteer = userRepository.findByEmail(volunteerEmail);

        if (volunteer == null || !"VOLUNTEER".equals(volunteer.getRole())) {
            throw new IllegalArgumentException(
                    "Valid volunteer account not found");
        }

        donation.setAssignedVolunteerEmail(volunteerEmail);
        donation.setPickupStatus("ASSIGNED");
        donation.setStatus("VOLUNTEER_ASSIGNED");

        donationRepository.saveDonation(donation);

        return donation;
    }
       public List<Donation> getAssignedDonations(String volunteerEmail) {

        return getAllDonations().stream()
                .filter(donation ->
                        volunteerEmail.equals(donation.getAssignedVolunteerEmail()))
                .toList();
    }

    public Donation updatePickupStatus(
            String donationId,
            String volunteerEmail,
            String newStatus) {

        Donation donation = getDonationById(donationId);

        if (!volunteerEmail.equals(donation.getAssignedVolunteerEmail())) {
            throw new SecurityException(
                    "You can only update donations assigned to you");
        }

        if ("PICKED_UP".equals(newStatus)) {

            if (!"ASSIGNED".equals(donation.getPickupStatus())) {
                throw new IllegalStateException(
                        "Donation must be assigned before pickup");
            }

        } else if ("DELIVERED".equals(newStatus)) {

            if (!"PICKED_UP".equals(donation.getPickupStatus())) {
                throw new IllegalStateException(
                        "Donation must be picked up before delivery");
            }

        } else {
            throw new IllegalArgumentException(
                    "Invalid pickup status. Use PICKED_UP or DELIVERED");
        }

        donation.setPickupStatus(newStatus);
        donation.setStatus(newStatus);

        donationRepository.saveDonation(donation);

        return donation;
    }
   public Donation getDonationById(String donationId) {

        Donation donation =
                donationRepository.findByDonationId(donationId);

        if (donation == null) {
            throw new IllegalArgumentException("Donation not found");
        }

        return donation;
    }

    public Donation updateDonation(
            String donationId,
            Donation updatedDonation,
            String donorEmail) {

        Donation existingDonation = getDonationById(donationId);

        if (!existingDonation.getDonorEmail().equals(donorEmail)) {
            throw new SecurityException(
                    "You can only update your own donations"
            );
        }

        existingDonation.setFoodName(updatedDonation.getFoodName());
        existingDonation.setFoodDescription(
                updatedDonation.getFoodDescription());
        existingDonation.setQuantity(updatedDonation.getQuantity());
        existingDonation.setQuantityUnit(updatedDonation.getQuantityUnit());
        existingDonation.setFoodType(updatedDonation.getFoodType());
        existingDonation.setPreparedAt(updatedDonation.getPreparedAt());
        existingDonation.setExpiryAt(updatedDonation.getExpiryAt());
        existingDonation.setPickupAddress(
                updatedDonation.getPickupAddress());

        donationRepository.saveDonation(existingDonation);

        return existingDonation;
    }

    public void deleteDonation(String donationId, String donorEmail) {

        Donation existingDonation = getDonationById(donationId);

        if (!existingDonation.getDonorEmail().equals(donorEmail)) {
            throw new SecurityException(
                    "You can only delete your own donations"
            );
        }

        donationRepository.deleteDonation(donationId);
    }
}
