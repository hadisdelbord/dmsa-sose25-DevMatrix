package com.parkandcharge.payment_service.dto;

import com.parkandcharge.payment_service.model.BookingStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookingDto {

    private Long bookingId; // Nullable for creation

    @NotNull(message = "Offer ID is required")
    @Positive(message = "Offer ID must be a positive number")
    private Long offerId;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Booking status is required")
    private BookingStatus bookingStatus;
}
