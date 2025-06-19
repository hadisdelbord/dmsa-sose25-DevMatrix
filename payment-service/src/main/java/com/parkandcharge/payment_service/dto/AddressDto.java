package com.parkandcharge.payment_service.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String state;
    private String city;
    private String street;
    private String postalCode;
}
