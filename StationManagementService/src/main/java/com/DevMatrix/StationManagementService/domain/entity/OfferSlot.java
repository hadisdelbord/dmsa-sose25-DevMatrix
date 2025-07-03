package com.DevMatrix.StationManagementService.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class OfferSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String timeSlot;
    private Float pricePerSlot;
    private Boolean isAvailable;
    private LocalDateTime slotDate;
    private int slotDuration;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private ChargingStation chargingStation;

    public OfferSlot(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for timeSlot
    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    // Getter and Setter for pricePerSlot
    public Float getPricePerSlot() {
        return pricePerSlot;
    }

    public void setPricePerSlot(Float pricePerSlot) {
        this.pricePerSlot = pricePerSlot;
    }

    public int getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(int slotDuration) {
        this.slotDuration = slotDuration;
    }

    // Getter and Setter for isAvailable
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // Getter and Setter for slotDate
    public LocalDateTime getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDateTime slotDate) {
        this.slotDate = slotDate;
    }

    // Getter and Setter for chargingStation
    public ChargingStation getChargingStation() {
        return chargingStation;
    }
     public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }
}
