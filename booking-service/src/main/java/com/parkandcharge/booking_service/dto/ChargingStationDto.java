package com.parkandcharge.booking_service.dto;

import com.parkandcharge.booking_service.model.ActivityStatus;
import lombok.Data;

@Data
public class ChargingStationDto {
    private Long id;
    private Long userId;
    private ActivityStatus activityStatus;
    private Float powerOutput;
    private Long addressId;
    private AddressDto address;
}
