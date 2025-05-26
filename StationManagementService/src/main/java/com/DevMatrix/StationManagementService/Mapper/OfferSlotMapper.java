package com.DevMatrix.StationManagementService.Mapper;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;


@Component
public class OfferSlotMapper {


    public OfferSlotDto toDto(OfferSlot offer){
        OfferSlotDto dto = new OfferSlotDto();
        dto.setId(offer.getId());
        dto.setTimeSlot(offer.getTimeSlot());
        dto.setPricePerSlot(offer.getPricePerSlot());
        dto.setIsAvailable(offer.getIsAvailable());
        dto.setSlotDate(offer.getSlotDate());
        if (offer.getChargingStation() != null)
            dto.setStationId(offer.getChargingStation().getId());
        return dto;
    }

        public OfferSlot toEntity(OfferSlotDto dto){
        OfferSlot offer = new OfferSlot();
        offer.setId(null);
        offer.setTimeSlot(dto.getTimeSlot());
        offer.setPricePerSlot(dto.getPricePerSlot());
        offer.setIsAvailable(dto.getIsAvailable());
        offer.setSlotDate(dto.getSlotDate());
        return offer;
    }


        public List<OfferSlotDto> toDtoList(List<OfferSlot> offers) {
        return offers.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());
    }

        public List<OfferSlot> toEntityList(List<OfferSlotDto> offerDtos) {
        return offerDtos.stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList());
    }
}
