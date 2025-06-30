package com.DevMatrix.StationManagementService.domain.entity;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class GeoLocation {
    private double latitude;
    private double longitude;

      public GeoLocation() {
        // Required by JPA
    }
    public GeoLocation(double latitude, double longitude){
         this.latitude = latitude;
         this.longitude = longitude;
    }

    public double getlatitude() {
        return latitude;
    }
      public double getlongitude() {
        return longitude;
    }
}
