package com.parkandcharge.booking_service.service;

import com.parkandcharge.booking_service.dto.OfferSlotDto;
import com.parkandcharge.booking_service.model.Booking;
import com.parkandcharge.booking_service.model.BookingStatus;
import com.parkandcharge.booking_service.repository.BookingRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Create a new booking and save it to the database.
     */
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Retrieve all bookings from the database.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Retrieve a specific booking by its ID.
     */
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Retrieve a specific booking by offer_id.
     */
   public List<Booking> getBookingsByOfferIds(List<Long> offerIds) {
        return bookingRepository.findByOfferIdIn(offerIds);
    }

    /**
     * Update an existing booking.
     */
    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id).map(booking -> {
            // Update booking details
            booking.setOfferId(updatedBooking.getOfferId());
            booking.setUserId(updatedBooking.getUserId());
            booking.setBookingStatus(updatedBooking.getBookingStatus());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    /**
     * Delete a booking by its ID.
     */
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    /**
     * Confirm a booking.
     * Can only confirm bookings with status RESERVED.
     */
    public Booking confirmBooking(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            if (booking.getBookingStatus() != BookingStatus.RESERVED) {
                throw new IllegalStateException("Only reserved bookings can be confirmed.");
            }
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    /**
     * Cancel a booking.
     * Can only cancel bookings with status RESERVED.
     */
    public Booking cancelBooking(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            if (booking.getBookingStatus() != BookingStatus.RESERVED) {
                throw new IllegalStateException("Only reserved bookings can be canceled.");
            }
            booking.setBookingStatus(BookingStatus.CANCELED);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    /**
     * Complete a booking.
     * Can only complete bookings with status CONFIRMED.
     */
    public Booking completeBooking(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
                throw new IllegalStateException("Only confirmed bookings can be completed.");
            }
            booking.setBookingStatus(BookingStatus.COMPLETED);
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    /**
     * Get all bookings made by a specific user.
     */
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
