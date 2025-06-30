package com.parkandcharge.booking_service.dto;

import com.parkandcharge.booking_service.model.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private double latitude;
    private double longitude;
}
