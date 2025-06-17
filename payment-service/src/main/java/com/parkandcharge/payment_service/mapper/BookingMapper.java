package com.parkandcharge.payment_service.mapper;

import org.springframework.stereotype.Component;

import com.parkandcharge.payment_service.dto.BookingDto;
import com.parkandcharge.payment_service.model.Booking;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setBookingId(booking.getBookingId());
        dto.setOfferId(booking.getOfferId());
        dto.setUserId(booking.getUserId());
        dto.setBookingStatus(booking.getBookingStatus());
        return dto;
    }

    public Booking toEntity(BookingDto dto) {
        Booking booking = new Booking();
        booking.setBookingId(dto.getBookingId()); 
        booking.setOfferId(dto.getOfferId());
        booking.setUserId(dto.getUserId());
        booking.setBookingStatus(dto.getBookingStatus());
        return booking;
    }
}
