package com.anadhanam.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Donation {

    private String donationId;
    private String donorEmail;
    private String foodName;
    private String foodDescription;
    private int quantity;
    private String quantityUnit;
    private String foodType;
    private String preparedAt;
    private String expiryAt;
    private String pickupAddress;
    private String status;
    private String createdAt;
    private String claimedByNgoEmail;
    private String claimedAt;
    private String assignedVolunteerEmail;
    private String pickupStatus;
    public Donation() {
    }

    @DynamoDbPartitionKey
    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getPreparedAt() {
        return preparedAt;
    }

    public void setPreparedAt(String preparedAt) {
        this.preparedAt = preparedAt;
    }

    public String getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(String expiryAt) {
        this.expiryAt = expiryAt;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getClaimedByNgoEmail() {
    return claimedByNgoEmail;
}

public void setClaimedByNgoEmail(String claimedByNgoEmail) {
    this.claimedByNgoEmail = claimedByNgoEmail;
}

public String getClaimedAt() {
    return claimedAt;
}

public void setClaimedAt(String claimedAt) {
    this.claimedAt = claimedAt;
}

public String getAssignedVolunteerEmail() {
    return assignedVolunteerEmail;
}

public void setAssignedVolunteerEmail(String assignedVolunteerEmail) {
    this.assignedVolunteerEmail = assignedVolunteerEmail;
}

public String getPickupStatus() {
    return pickupStatus;
}

public void setPickupStatus(String pickupStatus) {
    this.pickupStatus = pickupStatus;
}
}
