package com.fhdo.statisticsservice.controller;

import com.fhdo.statisticsservice.dto.BookingFilterRequestDTO;
import com.fhdo.statisticsservice.dto.BookingSummaryResponseDTO;
import com.fhdo.statisticsservice.service.BookingClient;
import com.fhdo.statisticsservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private StatisticsService statisticsService;

    // Called when user enters the statistics page
    @GetMapping("/bookings")
    public BookingSummaryResponseDTO getAllBookings(@RequestParam String ownerId) {
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
        return statisticsService.getFilteredBookingSummary(request);
    }
}

















//package com.fhdo.statisticsservice.controller;
//
//import com.fhdo.statisticsservice.dto.BookingFilterRequestDTO;
//import com.fhdo.statisticsservice.dto.BookingSummaryResponseDTO;
//import com.fhdo.statisticsservice.service.BookingClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/statistics")
//public class StatisticsController {
//
//    @Autowired
//    private BookingClient bookingClient;
//
//    @GetMapping("/bookings")
//    public BookingSummaryResponseDTO getAllBookings(@RequestParam String ownerId) {
//        return bookingClient.getBookingSummary(ownerId);
//    }
//
//    @PostMapping("/bookings/filter")
//    public BookingSummaryResponseDTO getFilteredBookings(@RequestBody BookingFilterRequestDTO request) {
//        return bookingClient.getFilteredBookingSummary(request);
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package com.fhdo.statisticsservice.controller;
////
////
////import com.fhdo.statisticsservice.dto.BookingDTO;
////import com.fhdo.statisticsservice.service.StatisticsService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.bind.annotation.RestController;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/statistics")
////public class StatisticsController {
////
////    private final StatisticsService statisticsService;
////
////    @Autowired
////    public StatisticsController(StatisticsService statisticsService) {
////        this.statisticsService = statisticsService;
////    }
////
//////    @GetMapping("/bookings")
//////    public List<BookingDTO> getBookings(@RequestParam String ownerId) {
//////        return statisticsService.getOwnerBookings(ownerId);
//////    }
////
////    @GetMapping("/init")
////    public ResponseEntity<String> initializeStatistics(@RequestParam String ownerId) {
////        statisticsService.cacheBookingsForOwner(ownerId);
////        return ResponseEntity.ok("Bookings cached for owner: " + ownerId);
////    }
////
////    @GetMapping("/view-all")
////    public List<BookingDTO> viewAllBookings(@RequestParam String ownerId) {
////        return statisticsService.getAllCachedBookings(ownerId);
////    }
////
////    @GetMapping("/by-station")
////    public List<BookingDTO> getBookingsByStation(
////            @RequestParam String ownerId,
////            @RequestParam String stationId
////    ) {
////        return statisticsService.getCachedBookingsByStation(ownerId, stationId);
////    }
////
////    @GetMapping("/by-date")
////    public List<BookingDTO> filterBookings(
////            @RequestParam String ownerId,
////            @RequestParam String stationId,
////            @RequestParam String period
////    ) {
////        return statisticsService.getFilteredBookings(ownerId, stationId, period);
////    }
////
////
////
////
////
////}
