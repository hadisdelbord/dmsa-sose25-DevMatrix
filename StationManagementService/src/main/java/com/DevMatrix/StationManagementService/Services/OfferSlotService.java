package com.DevMatrix.StationManagementService.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.DevMatrix.StationManagementService.Dtos.AvailableSlotDto;
import com.DevMatrix.StationManagementService.Dtos.OfferAndStationDto;
import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

public interface OfferSlotService {
    List<OfferSlotDto> getAll();
    Optional<OfferSlotDto> getById(Long id);
    void createOrUpdateAll(List<OfferSlotDto> dtos);
    OfferSlotDto update(Long id, OfferSlotDto dto);
    boolean delete(Long id);
    List<AvailableSlotDto> GetAvailableOffers();
    Optional<OfferAndStationDto> GetOfferWithStationById(long offerId);
    List<OfferSlotDto> GetByStationIdAndDate(Long stationId, LocalDate date);
    
}
