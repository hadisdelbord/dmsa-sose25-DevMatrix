package com.parkandcharge.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OfferSlotDto {
    private Long id;
    private LocalDateTime slotDate;
    private BigDecimal timeSlot;
    private Float pricePerSlot;
    private Boolean isAvailable;
    private ChargingStationDto chargingStation;
}
