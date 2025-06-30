package com.DevMatrix.StationManagementService.domain.entity;

import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;


@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private String city;
    private String street;
    @Embedded
    private PostalCode postalCode;
    @Embedded
    private GeoLocation location;

    @OneToMany(mappedBy = "address")
    private List<ChargingStation> chargingStations;

    public Address (){

    }

    @Builder
    public Address( String state, String city, String street, PostalCode postalCode, GeoLocation location,List<ChargingStation> chargingStations) {
    this.state = state;
    this.city = city;
    this.street = street;
    this.postalCode = postalCode;
    this.location = location;
    this.chargingStations = chargingStations;
}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Getter and Setter for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Getter and Setter for street
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // Getter and Setter for postalCode
    public PostalCode getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(PostalCode postalCode) {
        this.postalCode = postalCode;
    }

    // Getter and Setter for geoLocation
    public GeoLocation getGeoLocation() {
        return location;
    }

    public void setGeoLocation(GeoLocation location) {
        this.location = location;
    }

    // Getter and Setter for chargingStations
    public List<ChargingStation> getChargingStations() {
        return chargingStations;
    }
     public void setChargingStations(List<ChargingStation> chargingStations) {
        this.chargingStations = chargingStations;
    }
}
