package com.parkandcharge.booking_service.bootstrap_data;

import com.parkandcharge.booking_service.model.Booking;
import com.parkandcharge.booking_service.model.BookingStatus;
import com.parkandcharge.booking_service.repository.BookingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookingRepository bookingRepository;

    public DataInitializer(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
        if (bookingRepository.count() == 0) {
            bookingRepository.save(new Booking(null, 101L, 201L, BookingStatus.RESERVED));
            bookingRepository.save(new Booking(null, 102L, 202L, BookingStatus.CONFIRMED));
            bookingRepository.save(new Booking(null, 103L, 203L, BookingStatus.COMPLETED));
            bookingRepository.save(new Booking(null, 104L, 204L, BookingStatus.CANCELED));
            System.out.println("✅ Booking test data inserted.");
        }
    }
}
