package com.fhdo.statisticsservice.service;

import com.fhdo.statisticsservice.dto.BookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.*;

// @Service
// public class BookingClient {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String ownerId = authentication.getName();

//    @Autowired
//    private RestTemplate restTemplate;

//    // In-memory cache: ownerId -> List of bookings
//    private final Map<String, List<BookingDTO>> bookingCache = new HashMap<>();

//    // Fetch fresh bookings from the Booking Microservice
//    public List<BookingDTO> fetchBookingsFromService(String ownerId) {
//        String url = "http://booking-service/api/bookings/by-owner/" + ownerId;
//        ResponseEntity<BookingDTO[]> response = restTemplate.getForEntity(url, BookingDTO[].class);
//        BookingDTO[] bookingsArray = response.getBody();
//        return bookingsArray != null ? Arrays.asList(bookingsArray) : new ArrayList<>();
//    }

//    // Clear cache and fetch fresh data
//    public List<BookingDTO> getBookingSummary(String ownerId) {
//        // Clear cache to ensure fresh data
//        bookingCache.remove(ownerId);

//        // Fetch and cache
//        List<BookingDTO> bookings = fetchBookingsFromService(ownerId);
//        bookingCache.put(ownerId, bookings);

//        return bookings;
//    }

//    // Use cached data for filtering (to be implemented next)
//    public List<BookingDTO> getCachedBookings(String ownerId) {
//        return bookingCache.getOrDefault(ownerId, new ArrayList<>());
//    }
// }

@Service
public class BookingClient {
    // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // String ownerId = authentication.getName();

    // In-memory cache: ownerId -> List of bookings
    private final Map<String, List<BookingDTO>> bookingCache = new HashMap<>();

    // Simulate fetching bookings from the Booking Microservice
    public List<BookingDTO> fetchBookingsFromService(String ownerId) {
        List<BookingDTO> fakeBookings = new ArrayList<>();
        // String[] stations = {"station1", "station2", "station3"};
        // String[] customers = {"Alice", "Bob", "Charlie", "Diana", "Eve"};

        // for (int i = 0; i < 10; i++) {
        //     String station = stations[i % stations.length];
        //     String customer = customers[i % customers.length];
        //     double usage = 10 + i * 2;
        //     double pricePerKWh = 0.5 + (i % 3) * 0.1;
        //     double totalPrice = usage * pricePerKWh;

        //     fakeBookings.add(new BookingDTO(
        //             ownerId,
        //             station,
        //             totalPrice,
        //             pricePerKWh,
        //             "COMPLETED",
        //             usage,
        //             LocalDateTime.now().minusDays(i * 3),
        //             customer
        //     ));
        // }

       fakeBookings = List.of(
               new BookingDTO("123", "station1", 5.0, 0.5, "COMPLETED", 10.0, LocalDateTime.of(2025, 6, 22, 12, 0), "Alice"),
               new BookingDTO("124", "station2", 13.2, 0.6, "COMPLETED", 22.0, LocalDateTime.of(2025, 6, 19, 12, 0), "Bob"),
               new BookingDTO("123", "station3", 21.0, 0.7, "COMPLETED", 30.0, LocalDateTime.of(2025, 6, 16, 12, 0), "Charlie"),
               new BookingDTO("124", "station1", 14.0, 0.5, "COMPLETED", 28.0, LocalDateTime.of(2025, 6, 13, 12, 0), "Diana"),
               new BookingDTO("123", "station2", 22.0, 0.6, "COMPLETED", 36.0, LocalDateTime.of(2025, 6, 10, 12, 0), "Eve"),
               new BookingDTO("125", "station3", 16.0, 0.5, "COMPLETED", 32.0, LocalDateTime.of(2025, 6, 7, 12, 0), "Alice"),
               new BookingDTO("125", "station1", 30.8, 0.7, "COMPLETED", 44.0, LocalDateTime.of(2025, 6, 4, 12, 0), "Bob"),
               new BookingDTO("124", "station2", 28.0, 0.5, "COMPLETED", 56.0, LocalDateTime.of(2025, 6, 1, 12, 0), "Charlie"),
               new BookingDTO("126", "station3", 43.2, 0.6, "COMPLETED", 72.0, LocalDateTime.of(2025, 5, 29, 12, 0), "Diana"),
               new BookingDTO("124", "station1", 49.0, 0.7, "COMPLETED", 70.0, LocalDateTime.of(2025, 5, 26, 12, 0), "Eve")
       );


        return fakeBookings;
    }

    // Clear cache and fetch fresh data
    public List<BookingDTO> getBookingSummary(String ownerId) {
        bookingCache.remove(ownerId);
        List<BookingDTO> bookings = fetchBookingsFromService(ownerId);
        bookingCache.put(ownerId, bookings);
        return bookings;
    }

    // Use cached data for filtering
    public List<BookingDTO> getCachedBookings(String ownerId) {
        return bookingCache.getOrDefault(ownerId, new ArrayList<>());
    }
}