package com.DevMatrix.StationManagementService.Mapper;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.Dtos.AvailableSlotDto;
import com.DevMatrix.StationManagementService.Dtos.OfferAndStationDto;
import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.domain.entity.Address;
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

    public AvailableSlotDto toDtoWithAddress(OfferSlot offer) {
    AvailableSlotDto availableDto = new AvailableSlotDto();
    OfferSlotDto dto = toDto(offer); // use the default mapping
    if (offer.getChargingStation() != null && offer.getChargingStation().getAddress() != null) {
        Address address = offer.getChargingStation().getAddress();
        availableDto.setOfferId(dto.getId());
        availableDto.setStationName(offer.getChargingStation().getName());
        availableDto.setPowerOutput(offer.getChargingStation().getPowerOutput());
        availableDto.setTimeSlot(dto.getTimeSlot());
        availableDto.setPrice(dto.getPricePerSlot());
        availableDto.setAvailableDate(dto.getSlotDate());
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(address.getCity());
        addressDto.setStreet(address.getStreet());
        addressDto.setPostalCode(address.getPostalCode());
        availableDto.setAddress(addressDto);
    }
    return availableDto;
    }

    public OfferAndStationDto toDtoWithStation(OfferSlot offer) {
    OfferAndStationDto offerStationDto = new OfferAndStationDto();
    OfferSlotDto dto = toDto(offer); // use the default mapping
    if (offer.getChargingStation() != null) {
        offerStationDto.setStationName(offer.getChargingStation().getName());
        offerStationDto.setTimeSlot(dto.getTimeSlot());
        offerStationDto.setPrice(dto.getPricePerSlot());
        offerStationDto.setAvailableDate(dto.getSlotDate());
    }
    return offerStationDto;
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
    public List<AvailableSlotDto> toDtoListWithAddress(List<OfferSlot> offers) {
        return offers.stream()
                        .map(this::toDtoWithAddress)
                        .collect(Collectors.toList());
    }

        public List<OfferSlot> toEntityList(List<OfferSlotDto> offerDtos) {
        return offerDtos.stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList());
    }
}
