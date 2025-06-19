package com.parkandcharge.payment_service.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parkandcharge.payment_service.dto.AddressDto;
import com.parkandcharge.payment_service.dto.BookingDto;
import com.parkandcharge.payment_service.dto.BookingViewDto;
import com.parkandcharge.payment_service.dto.ChargingStationDto;
import com.parkandcharge.payment_service.dto.OfferSlotDto;
import com.parkandcharge.payment_service.mapper.BookingMapper;
import com.parkandcharge.payment_service.model.Booking;
import com.parkandcharge.payment_service.service.BookingService;
import com.parkandcharge.payment_service.service.OfferClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private OfferClient offerClient;

    @Autowired
    private BookingMapper bookingMapper;

    /**
     * Create a new booking with validation.
     */
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid BookingDto dto) {
        Booking saved = bookingService.createBooking(bookingMapper.toEntity(dto));
        return ResponseEntity.ok(bookingMapper.toDto(saved));
    }

    /**
     * Retrieve all bookings.
     */
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> dtos = bookingService.getAllBookings()
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get a booking by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(bookingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a booking by its ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingDto dto) {
        Booking updated = bookingService.updateBooking(id, bookingMapper.toEntity(dto));
        return ResponseEntity.ok(bookingMapper.toDto(updated));
    }

    /**
     * Delete a booking by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Confirm a booking.
     */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingDto> confirmBooking(@PathVariable Long id) {
        Booking booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(bookingMapper.toDto(booking));
    }

    /**
     * Complete a booking.
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingDto> completeBooking(@PathVariable Long id) {
        Booking booking = bookingService.completeBooking(id);
        return ResponseEntity.ok(bookingMapper.toDto(booking));
    }

    /**
     * Cancel a booking.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(bookingMapper.toDto(booking));
    }

    /**
     * Get bookings for a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingDto> dtos = bookingService.getBookingsByUserId(userId)
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/user/{userId}/details")
    public ResponseEntity<List<BookingViewDto>> getUserBookingDetails(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);

        List<BookingViewDto> result = bookings.stream().map(booking -> {
            OfferSlotDto offer = offerClient.getOfferById(booking.getOfferId());

            BookingViewDto dto = new BookingViewDto();
            dto.setBookingId(booking.getBookingId());
            dto.setBookingStatus(booking.getBookingStatus());
            dto.setSlotDate(offer.getSlotDate());
            dto.setPricePerSlot(offer.getPricePerSlot());
            dto.setTimeSlot(offer.getTimeSlot());

            ChargingStationDto station = offer.getChargingStation();
            if (station != null) {
                dto.setStationName("Station #" + station.getId()); // Or real name if you have it
                dto.setPowerOutput(station.getPowerOutput());

                AddressDto addr = station.getAddress();
                if (addr != null) {
                    dto.setCity(addr.getCity());
                    dto.setStreet(addr.getStreet());
                    dto.setPostalCode(addr.getPostalCode());
                }
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(result);
    }

}
