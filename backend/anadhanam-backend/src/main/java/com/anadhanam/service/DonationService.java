package com.anadhanam.service;

import com.anadhanam.model.Donation;
import com.anadhanam.repository.DonationRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DonationService {

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation createDonation(Donation donation, String donorEmail) {

        donation.setDonationId(UUID.randomUUID().toString());
        donation.setDonorEmail(donorEmail);
        donation.setStatus("AVAILABLE");
        donation.setCreatedAt(Instant.now().toString());

        donationRepository.saveDonation(donation);

        return donation;
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAllDonations();
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
