package com.DevMatrix.StationManagementService.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.PostalCode;


public interface AddressService {
    AddressDto createAddress(AddressDto address);
    Optional<AddressDto> getAddressById(Long id);
    Optional<AddressDto> GetAddressByPostalCode(PostalCode postalCode);
    List<AddressDto> getAllAddresses();
    void deleteAddress(Long id);
}
