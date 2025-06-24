package com.fhdo.statisticsservice.dto;

import java.util.List;

public class BookingFilterRequestDTO {
    private String ownerId;
    private List<String> stationIds;
    private String dateRange; // e.g., "last_month", "last_week"

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getStationIds() {
        return stationIds;
    }

    public void setStationIds(List<String> stationIds) {
        this.stationIds = stationIds;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}
