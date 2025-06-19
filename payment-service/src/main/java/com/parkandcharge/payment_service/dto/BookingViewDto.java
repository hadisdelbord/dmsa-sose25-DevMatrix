package com.parkandcharge.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.parkandcharge.payment_service.model.BookingStatus;

@Data
public class BookingViewDto {
    private Long bookingId;
    private BookingStatus bookingStatus;

    private LocalDateTime slotDate;
    private Float pricePerSlot;
    private BigDecimal timeSlot;

    private String stationName;
    private Float powerOutput;

    private String city;
    private String street;
    private String postalCode;
}
