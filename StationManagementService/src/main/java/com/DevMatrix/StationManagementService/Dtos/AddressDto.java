package com.DevMatrix.StationManagementService.Dtos;

import com.DevMatrix.StationManagementService.domain.entity.PostalCode;

import io.github.resilience4j.core.lang.Nullable;
import jakarta.persistence.Embedded;

public class AddressDto {
    private long id;
    private String state;
    private String city;
    private String street;

    @Embedded
    private PostalCode postalCode;

    @Nullable
    private Double latitude;
    
    @Nullable
    private Double longitude;
    // private List<ChargingStationDto> chargingStations;

    // Getters
    public long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public PostalCode getPostalCode() {
        return postalCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    // public List<ChargingStationDto> getChargingStations() {
    // return chargingStations;
    // }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(PostalCode postalCode) {
        this.postalCode = postalCode;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // public void setChargingStations(List<ChargingStationDto> chargingStations) {
    // this.chargingStations = chargingStations;
    // }
}
