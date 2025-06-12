package com.DevMatrix.StationManagementService.Dtos;


import com.DevMatrix.StationManagementService.domain.entity.PostalCode;




public class AddressDto {
    private long id;
    private String state;
    private String city;
    private String street;
    private PostalCode postalCode; 
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

    // public List<ChargingStationDto> getChargingStations() {
    //     return chargingStations;
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

    // public void setChargingStations(List<ChargingStationDto> chargingStations) {
    //     this.chargingStations = chargingStations;
    // }
}
