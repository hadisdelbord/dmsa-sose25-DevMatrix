package com.DevMatrix.StationManagementService.Services;

import java.util.List;


import com.DevMatrix.StationManagementService.Dtos.ChargingStationDto;

public interface ChargingStationService {
    List<ChargingStationDto> getAllStations(String email);
    List<ChargingStationDto> GetStationsByUserId(Long userId);
    ChargingStationDto getStationById(Long id);
    ChargingStationDto createStation(ChargingStationDto dto, String email);
    ChargingStationDto updateStation(Long id, ChargingStationDto dto);
    boolean deleteStation(Long id);
}
