package com.fhdo.statisticsservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public class BookingDTO {
    private String ownerID;
    private String stationId;
    private double totalPrice;
    private double pricePerKWh;
    private String status;
    private double usageKWh;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    private String customerName;

    public BookingDTO(String ownerID,
                      String stationId,
                      double totalPrice,
                      double pricePerKWh,
                      String status,
                      double usageKWh,
                      LocalDateTime dateTime,
                      String customerName) {
        this.ownerID = ownerID;
        this.stationId = stationId;
        this.totalPrice = totalPrice;
        this.pricePerKWh = pricePerKWh;
        this.status = status;
        this.usageKWh = usageKWh;
        this.dateTime = dateTime;
        this.customerName = customerName;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getUsageKWh() {
        return usageKWh;
    }

    public void setUsageKWh(double usageKWh) {
        this.usageKWh = usageKWh;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

