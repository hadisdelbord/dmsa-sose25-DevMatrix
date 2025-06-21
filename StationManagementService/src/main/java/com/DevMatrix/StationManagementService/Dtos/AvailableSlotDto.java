package com.DevMatrix.StationManagementService.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableSlotDto {
    private Long offerId;
    private String stationName;
    private Float powerOutput;
    private String timeSlot;
    private Float price;
    private LocalDateTime availableDate;
    private int slotDuration;
    private AddressDto address;

}
