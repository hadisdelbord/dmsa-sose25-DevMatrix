package com.DevMatrix.StationManagementService.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.mapstruct.ap.shaded.freemarker.core._UnexpectedTypeErrorExplainerTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.Dtos.ChargingStationDto;
import com.DevMatrix.StationManagementService.Mapper.AddressMapper;
import com.DevMatrix.StationManagementService.Repositories.IAddressRepository;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.ChargingStation;
import com.DevMatrix.StationManagementService.domain.entity.GeoLocation;
import com.DevMatrix.StationManagementService.domain.entity.PostalCode;

@Service
public class AddressServiceImp implements AddressService {

    @Autowired
    private MapService _mapService;

    @Autowired
    private IAddressRepository _addressRepository;

    @Autowired
    private AddressMapper _mapper;

    public AddressServiceImp(IAddressRepository addressRepository, AddressMapper mapper, MapService mapService){
        this._addressRepository = addressRepository;
        this._mapper = mapper;
        this._mapService = mapService;
    }
    
    @Override
    public AddressDto createAddress(AddressDto addressdto) {
        var address = _mapper.toEntity(addressdto);
        var mapLocation = _mapService.getLocationFromAddress(address);
        address.setGeoLocation(new GeoLocation(mapLocation.getLatitude(), mapLocation.getLongitude()));
        var savedAddress = _addressRepository.save(address);
        return _mapper.toDto(address);
    }

    @Override
    public AddressDto updateAddress(Long id, AddressDto addressdto){
        var currentAddress = _addressRepository.findById(id).get();
        if(currentAddress != null){
            Address address = _mapper.toEntity(addressdto);
            currentAddress.setPostalCode(address.getPostalCode());
            currentAddress.setState(address.getState());
            currentAddress.setCity(address.getCity());
            currentAddress.setStreet(address.getStreet());
            var mapLocation = _mapService.getLocationFromAddress(address);
            currentAddress.setGeoLocation(new GeoLocation(mapLocation.getLatitude(), mapLocation.getLongitude()));
            Address savedAddress = _addressRepository.save(currentAddress);
            AddressDto savedAddressDto = _mapper.toDto(savedAddress);
            savedAddressDto.setId(addressdto.getId());
            return savedAddressDto;
        }
        return null;
    }

    @Override
    public Optional<AddressDto> getAddressById(Long id) {
        var address = _addressRepository.findById(id);
        return _addressRepository.findById(id).map(_mapper :: toDto);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> stations = StreamSupport
         .stream(_addressRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
            return _mapper.toDtoList(stations);
    }

    @Override
    public void deleteAddress(Long id) {
        _addressRepository.findById(id).ifPresent(_addressRepository::delete);
    }

    @Override
    public Optional<AddressDto> GetAddressByPostalCode(PostalCode postalCode) {
        return _addressRepository.findByPostalCode(postalCode.getValue()).map(_mapper :: toDto);
    }
}
