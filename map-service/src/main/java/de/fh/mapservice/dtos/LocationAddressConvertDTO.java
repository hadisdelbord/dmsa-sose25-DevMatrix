package de.fh.mapservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LocationAddressConvertDTO {

    @JsonProperty("state")
    private String state;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("postalCode")
    private PostalCodeDTO postalCode;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public PostalCodeDTO getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(PostalCodeDTO postalCode) {
        this.postalCode = postalCode;
    }
}
