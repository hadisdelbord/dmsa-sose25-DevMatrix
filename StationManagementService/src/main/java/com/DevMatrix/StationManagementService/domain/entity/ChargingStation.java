package com.DevMatrix.StationManagementService.domain.entity;

import java.util.List;

import com.DevMatrix.StationManagementService.domain.Enums.ActivityStatus;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class ChargingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus;
    private Float powerOutput;

   @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "chargingStation")
    private List<OfferSlot> offerSlots;

    public ChargingStation(){

    }
    public ChargingStation(List<OfferSlot> offerSlots){
        this.offerSlots = offerSlots;
    }

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

    // Getter and Setter for address
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // Getter and Setter for offerSlots
    public List<OfferSlot> getOfferSlots() {
        return offerSlots;
    }
     public void setOfferSlots(List<OfferSlot> offerSlots) {
        this.offerSlots = offerSlots;
    }
}
