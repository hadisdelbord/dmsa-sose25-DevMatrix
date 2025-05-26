package com.DevMatrix.StationManagementService.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.ChargingStation;

@Component
public class AddressMapper {

    @Autowired
    private ChargingStationMaper chargingStationMapper;

    public AddressDto toDto(Address address){
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setState(address.getState());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setPostalCode(address.getPostalCode());
        if (address.getChargingStations() != null)
            dto.setChargingStations(
                chargingStationMapper.toDtoList(address.getChargingStations())
            );
        return dto;
    }

        public Address toEntity(AddressDto dto){
        Address address = new Address();
        address.setId(null);
        address.setState(dto.getState());
        address.setCity(dto.getCity());
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
         if (dto.getChargingStations() != null)
            address.setChargingStations(
                chargingStationMapper.toEntityList(dto.getChargingStations())
            );
        return address;
    }
        public List<AddressDto> toDtoList(List<Address> addresses) {
        return addresses.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());
    }
        public List<Address> toEntityList(List<AddressDto> addresses) {
        return addresses.stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList());
    }
}
