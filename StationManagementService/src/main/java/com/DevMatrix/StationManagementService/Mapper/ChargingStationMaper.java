package com.DevMatrix.StationManagementService.Mapper;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


import com.DevMatrix.StationManagementService.Dtos.ChargingStationDto;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.ChargingStation;


@Component
public class ChargingStationMaper {

    public ChargingStationDto toDto(ChargingStation station){
        ChargingStationDto dto = new ChargingStationDto();
        dto.setId(station.getId());
        dto.setUserId(station.getUserId());
        dto.setActivityStatus(station.getActivityStatus());
        dto.setPowerOutput(station.getPowerOutput());
        if (station.getAddress() != null)
            dto.setAddressId(station.getAddress().getId());
        return dto;
    }

        public ChargingStation toEntity(ChargingStationDto dto){
        ChargingStation station = new ChargingStation();
        station.setId(null);
        station.setUserId(dto.getUserId());
        station.setActivityStatus(dto.getActivityStatus());
        station.setPowerOutput(dto.getPowerOutput());
        return station;
    }


        public List<ChargingStationDto> toDtoList(List<ChargingStation> stations) {
        return stations.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());
    }

        public List<ChargingStation> toEntityList(List<ChargingStationDto> stations) {
        return stations.stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList());
    }
}
