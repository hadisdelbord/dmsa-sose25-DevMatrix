package com.DevMatrix.StationManagementService.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevMatrix.StationManagementService.Dtos.AddressDto;
import com.DevMatrix.StationManagementService.Dtos.ChargingStationDto;
import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.Dtos.UserResponse;
import com.DevMatrix.StationManagementService.Mapper.ChargingStationMaper;
import com.DevMatrix.StationManagementService.Mapper.OfferSlotMapper;
import com.DevMatrix.StationManagementService.Repositories.ChargingStationRepository;
import com.DevMatrix.StationManagementService.Repositories.IAddressRepository;
import com.DevMatrix.StationManagementService.Repositories.OfferSlotRepository;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import com.DevMatrix.StationManagementService.domain.entity.ChargingStation;
import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

@Service
public class ChargingStationServiceImp implements ChargingStationService {
    @Autowired
    private ChargingStationRepository _chargingStationRepository;
    @Autowired
    private ChargingStationMaper _chargingStationMapper;
    @Autowired
    private IAddressRepository _addressRepository;
    @Autowired
    private final userClient _userClient;
    @Autowired
    private OfferSlotRepository _offerSlotRepository;
    @Autowired
    private OfferSlotMapper _OfferSlotMapper;

    public ChargingStationServiceImp(ChargingStationRepository chargingStationRepository,
            ChargingStationMaper ChargingStationMaper, IAddressRepository addressRepository, userClient userClient) {
        this._chargingStationRepository = chargingStationRepository;
        this._chargingStationMapper = ChargingStationMaper;
        this._addressRepository = addressRepository;
        this._userClient = userClient;
    }

    @Override
    public List<ChargingStationDto> getAllStations(String email) {
        UserResponse user = _userClient.GetUserDataByEmail(email);
        Long userId = user.getUserId();
        List<ChargingStation> stations = (List<ChargingStation>) _chargingStationRepository.findByUserId(userId);
        var stationDtos = _chargingStationMapper.toDtoList(stations);
        for (ChargingStationDto station : stationDtos) {
            _addressRepository.findById(station.getAddressId()).ifPresent(address -> {
                AddressDto addressDto = new AddressDto();
                addressDto.setId(address.getId());
                addressDto.setState(address.getState());
                addressDto.setCity(address.getCity());
                addressDto.setStreet(address.getStreet());
                addressDto.setPostalCode(address.getPostalCode());
                station.setAddress(addressDto);
            });
        }
        return stationDtos;
    }

    // get stations for map
    public List<ChargingStationDto> getStationsForMap() {
        List<ChargingStation> stations = (List<ChargingStation>) _chargingStationRepository.findAll();
        var stationDtos = _chargingStationMapper.toDtoList(stations);
        for (ChargingStationDto station : stationDtos) {
            _addressRepository.findById(station.getAddressId()).ifPresent(address -> {
                AddressDto addressDto = new AddressDto();
                addressDto.setId(address.getId());
                addressDto.setState(address.getState());
                addressDto.setCity(address.getCity());
                addressDto.setStreet(address.getStreet());
                addressDto.setPostalCode(address.getPostalCode());
                addressDto.setLatitude(address.getLatitude());
                addressDto.setLongitude(address.getLongitude());
                station.setAddress(addressDto);
            });
        }
        return stationDtos;

    }

    @Override
    public List<ChargingStationDto> GetStationsByUserId(Long userId) {
        // Step 1: get stations by userId
        List<ChargingStation> stations = _chargingStationRepository.findByUserId(userId);

        // Step 2: map to DTOs (basic fields)
        List<ChargingStationDto> stationDtos = _chargingStationMapper.toDtoList(stations);

        // Step 3: enrich each station with address + offerSlots
        for (ChargingStationDto station : stationDtos) {
            // Load and set address
            _addressRepository.findById(station.getAddressId()).ifPresent(address -> {
                AddressDto addressDto = new AddressDto();
                addressDto.setId(address.getId());
                addressDto.setState(address.getState());
                addressDto.setCity(address.getCity());
                addressDto.setStreet(address.getStreet());
                addressDto.setPostalCode(address.getPostalCode());
                station.setAddress(addressDto);
            });

            // Load and set offerSlots
            List<OfferSlot> offerSlots = _offerSlotRepository.findByChargingStationId(station.getId());
            List<OfferSlotDto> offerSlotDtos = _OfferSlotMapper.toDtoList(offerSlots);
            station.setOfferSlots(offerSlotDtos);
        }

        return stationDtos;
    }

    @Override
    public ChargingStationDto getStationById(Long id) {
        var station = _chargingStationRepository.findById(id);
        var stationDto = _chargingStationMapper.toDto(station.get());
        var addressId = stationDto.getAddressId();
        _addressRepository.findById(addressId).ifPresent(address -> {
            AddressDto addressDto = new AddressDto();
            addressDto.setId(address.getId());
            addressDto.setState(address.getState());
            addressDto.setCity(address.getCity());
            addressDto.setStreet(address.getStreet());
            addressDto.setPostalCode(address.getPostalCode());
            stationDto.setAddress(addressDto);
        });
        return stationDto;
    }

    @Override
    public ChargingStationDto createStation(ChargingStationDto dto, String email) {
        ChargingStation station = _chargingStationMapper.toEntity(dto);
        UserResponse user = _userClient.GetUserDataByEmail(email);
        station.setUserId(user.getUserId());
        var savedStation = _chargingStationRepository.save(station);
        var savedStationDto = _chargingStationMapper.toDto(savedStation);
        savedStationDto.setAddressId(dto.getAddressId());
        return savedStationDto;
    }

    @Override
    public ChargingStationDto updateStation(Long id, ChargingStationDto dto) {
        var currentchargingStation = _chargingStationRepository.findById(id).get();
        if (currentchargingStation != null) {
            ChargingStation station = _chargingStationMapper.toEntity(dto);
            currentchargingStation.setActivityStatus(station.getActivityStatus());
            currentchargingStation.setPowerOutput(station.getPowerOutput());
            currentchargingStation.setName(station.getName());
            if (dto.getAddress() != null) {
                Address address = new Address();
                address.setId(dto.getAddress().getId());
                currentchargingStation.setAddress(address);
            }
            ChargingStation savedStation = _chargingStationRepository.save(currentchargingStation);
            ChargingStationDto savedStationDto = _chargingStationMapper.toDto(savedStation);
            savedStationDto.setAddressId(dto.getAddressId());
            return savedStationDto;
        }
        return null;
    }

    @Override
    public boolean deleteStation(Long id) {
        if (_chargingStationRepository.existsById(id)) {
            _chargingStationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
