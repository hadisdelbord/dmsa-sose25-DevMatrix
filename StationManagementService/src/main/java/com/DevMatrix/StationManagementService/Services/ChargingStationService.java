package com.DevMatrix.StationManagementService.Services;

import java.util.List;


import com.DevMatrix.StationManagementService.Dtos.ChargingStationDto;

public interface ChargingStationService {
    List<ChargingStationDto> getAllStations();
    ChargingStationDto getStationById(Long id);
    ChargingStationDto createStation(ChargingStationDto dto);
    ChargingStationDto updateStation(Long id, ChargingStationDto dto);
    boolean deleteStation(Long id);
}
