package com.DevMatrix.StationManagementService.Dtos;

import java.util.List;


import com.DevMatrix.StationManagementService.domain.Enums.ActivityStatus;
import com.DevMatrix.StationManagementService.domain.entity.Address;




public class ChargingStationDto {
    private Long id;
    private Long userId;
    private ActivityStatus activityStatus;
    private String name;
    private Float powerOutput;
    private Long addressId;
     private List<OfferSlotDto> offerSlots;
     private AddressDto address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter and Setter for activityStatus
    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(ActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
    }

    // Getter and Setter for powerOutput
    public Float getPowerOutput() {
        return powerOutput;
    }

    public void setPowerOutput(Float powerOutput) {
        this.powerOutput = powerOutput;
    }

    // Getter and Setter for addressId
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    // Getter and Setter for address
    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    // Getter and Setter for offerSlots
    public List<OfferSlotDto> getOfferSlots() {
        return offerSlots;
    }
     public void setOfferSlots(List<OfferSlotDto> offerSlots) {
        this.offerSlots = offerSlots;
    }
}
