package com.DevMatrix.StationManagementService.domain.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.List;

import com.DevMatrix.StationManagementService.domain.Enums.ActivityStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class OfferSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal timeSlot;
    private Float pricePerSlot;
    private Boolean isAvailable;
    private LocalDateTime slotDate;

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
    public BigDecimal getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(BigDecimal timeSlot) {
        this.timeSlot = timeSlot;
    }

    // Getter and Setter for pricePerSlot
    public Float getPricePerSlot() {
        return pricePerSlot;
    }

    public void setPricePerSlot(Float pricePerSlot) {
        this.pricePerSlot = pricePerSlot;
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
