package com.DevMatrix.StationManagementService.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferSlotDto {
    private Long id;
    private Long stationId;
    private LocalDateTime slotDate;
    private BigDecimal timeSlot;
    private Float pricePerSlot;
    private Boolean isAvailable;
    private int slotDuration;

    private AddressDto address;

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

    public int getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(int slotDuration) {
        this.slotDuration = slotDuration;
    }
   
    //  public void setChargingStation(ChargingStationDto chargingStation) {
    //     this.chargingStation = chargingStation;
    // }

    public Long getStationId() {
        return stationId;
    }
     public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public AddressDto getAddress() {
        return address;
    }
     public void setAddress(AddressDto addressdto) {
        this.address = addressdto;
    }
}
