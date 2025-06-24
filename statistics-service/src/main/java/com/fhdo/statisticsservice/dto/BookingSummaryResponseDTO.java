package com.fhdo.statisticsservice.dto;

import java.util.List;

public class BookingSummaryResponseDTO {
    private List<BookingDTO> bookings;
    private double totalPrice;
    private double totalUsageKWh;
    private double pricePerKWh;
    private List<String> stationNames;

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPricePerKWh() {
        return pricePerKWh;
    }

    public void setPricePerKWh(double pricePerKWh) {
        this.pricePerKWh = pricePerKWh;
    }

    public double getTotalUsageKWh() {
        return totalUsageKWh;
    }

    public void setTotalUsageKWh(double totalUsageKWh) {
        this.totalUsageKWh = totalUsageKWh;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public void setStationNames(List<String> stationNames) {
        this.stationNames = stationNames;
    }
}
