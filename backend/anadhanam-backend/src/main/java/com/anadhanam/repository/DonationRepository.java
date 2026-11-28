package com.anadhanam.repository;

import com.anadhanam.model.Donation;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DonationRepository {

    private final DynamoDbTable<Donation> donationTable;

    public DonationRepository(DynamoDbEnhancedClient enhancedClient) {
        this.donationTable = enhancedClient.table(
                "AnadhanamDonations",
                TableSchema.fromBean(Donation.class)
        );
    }

    public void saveDonation(Donation donation) {
        donationTable.putItem(donation);
    }

    public Donation findByDonationId(String donationId) {

        Key key = Key.builder()
                .partitionValue(donationId)
                .build();

        return donationTable.getItem(key);
    }

    public List<Donation> findAllDonations() {

        List<Donation> donations = new ArrayList<>();

        donationTable.scan().items().forEach(donations::add);

        return donations;
    }

    public void deleteDonation(String donationId) {

        Key key = Key.builder()
                .partitionValue(donationId)
                .build();

        donationTable.deleteItem(key);
    }
}
