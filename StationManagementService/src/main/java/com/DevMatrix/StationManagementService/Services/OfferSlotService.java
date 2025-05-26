package com.DevMatrix.StationManagementService.Services;

import java.util.List;
import java.util.Optional;

import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;

public interface OfferSlotService {
    List<OfferSlotDto> getAll();
    Optional<OfferSlotDto> getById(Long id);
    OfferSlotDto create(OfferSlotDto dto);
    OfferSlotDto update(Long id, OfferSlotDto dto);
    boolean delete(Long id);
    
}
