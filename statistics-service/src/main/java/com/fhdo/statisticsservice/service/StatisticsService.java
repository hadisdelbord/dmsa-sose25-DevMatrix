package com.fhdo.statisticsservice.service;

import com.fhdo.statisticsservice.dto.BookingDTO;
import com.fhdo.statisticsservice.dto.BookingFilterRequestDTO;
import com.fhdo.statisticsservice.dto.BookingSummaryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private BookingClient bookingClient;

    public BookingSummaryResponseDTO getFilteredBookingSummary(BookingFilterRequestDTO request) {
        List<BookingDTO> allBookings = bookingClient.getCachedBookings(request.getOwnerId());

        // Filter by station
        List<BookingDTO> filtered = allBookings;
        if (request.getStationIds() != null && !request.getStationIds().isEmpty()) {
            filtered = filtered.stream()
                    .filter(b -> request.getStationIds().contains(b.getStationId()))
                    .collect(Collectors.toList());
        }

        // Filter by date range
        if (request.getDateRange() != null && !request.getDateRange().isEmpty()) {
            LocalDateTime startDate = getStartDate(request.getDateRange());
            filtered = filtered.stream()
                    .filter(b -> b.getDateTime().isAfter(startDate))
                    .collect(Collectors.toList());
        }

        // Calculate totals
        double totalPrice = filtered.stream().mapToDouble(BookingDTO::getTotalPrice).sum();
        double totalUsage = filtered.stream().mapToDouble(BookingDTO::getUsageKWh).sum();
        double pricePerKWh = totalUsage > 0 ? totalPrice / totalUsage : 0.0;

        // Extract station names for dropdown
        List<String> stationNames = allBookings.stream()
                .map(BookingDTO::getStationId)
                .distinct()
                .collect(Collectors.toList());

        // Build response
        BookingSummaryResponseDTO response = new BookingSummaryResponseDTO();
        response.setBookings(filtered);
        response.setTotalPrice(totalPrice);
        response.setTotalUsageKWh(totalUsage);
        response.setPricePerKWh(pricePerKWh);
        response.setStationNames(stationNames);

        return response;
    }

    private LocalDateTime getStartDate(String range) {
        LocalDateTime now = LocalDateTime.now();
        return switch (range) {
            case "last_year" -> now.minusYears(1);
            case "last_6_months" -> now.minusMonths(6);
            case "last_3_months" -> now.minusMonths(3);
            case "last_month" -> now.minusMonths(1);
            case "last_week" -> now.minusWeeks(1);
            case "last_day" -> now.minusDays(1);
            default -> now.minusYears(100); // fallback to include all
        };
    }
}
