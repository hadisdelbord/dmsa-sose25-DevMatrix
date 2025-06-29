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
public class OfferAndStationDto {
    private String stationName;
    private String timeSlot;
    private Float price;
    private LocalDateTime availableDate;
}
