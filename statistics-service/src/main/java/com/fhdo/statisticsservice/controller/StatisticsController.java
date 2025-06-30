package com.fhdo.statisticsservice.controller;

import com.fhdo.statisticsservice.dto.BookingFilterRequestDTO;
import com.fhdo.statisticsservice.dto.BookingSummaryResponseDTO;
import com.fhdo.statisticsservice.service.BookingClient;
import com.fhdo.statisticsservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private StatisticsService statisticsService;

    // Called when user enters the statistics page
    @GetMapping("/bookings")
    public BookingSummaryResponseDTO getAllBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerId = authentication.getName();
        // Clear cache and fetch fresh data
        bookingClient.getBookingSummary(ownerId);

        // Return full summary (unfiltered)
        BookingFilterRequestDTO request = new BookingFilterRequestDTO();
        request.setOwnerId(ownerId);
        return statisticsService.getFilteredBookingSummary(request);
    }

    // Called when user applies filters
    @PostMapping("/bookings/filter")
    public BookingSummaryResponseDTO getFilteredBookings(@RequestBody BookingFilterRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerId = authentication.getName();
        request.setOwnerId(ownerId);  // <--- THIS LINE IS IMPORTANT
        return statisticsService.getFilteredBookingSummary(request);
    }
}
