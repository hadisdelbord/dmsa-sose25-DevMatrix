package com.DevMatrix.StationManagementService.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.Mapper.OfferSlotMapper;
import com.DevMatrix.StationManagementService.Repositories.ChargingStationRepository;
import com.DevMatrix.StationManagementService.Repositories.OfferSlotRepository;
import com.DevMatrix.StationManagementService.domain.entity.ChargingStation;
import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

@Service
public class OfferSlotServiceImp implements OfferSlotService {
    @Autowired
    private OfferSlotRepository _offerSlotRepository;
    @Autowired
    private OfferSlotMapper _OfferSlotMapper;
    @Autowired
    private ChargingStationRepository _chargingStationRepository;

    public OfferSlotServiceImp(OfferSlotRepository offerSlotRepository, OfferSlotMapper offerSlotMapper, ChargingStationRepository chargingStationRepository){
        this._offerSlotRepository = offerSlotRepository;
        this._OfferSlotMapper = offerSlotMapper;
        this._chargingStationRepository = chargingStationRepository;
    }

    @Override
    public List<OfferSlotDto> getAll() {
        List<OfferSlot> lstOfferSlots = StreamSupport
        .stream(_offerSlotRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
        return _OfferSlotMapper.toDtoList(lstOfferSlots);
    }
    @Override
    public Optional<OfferSlotDto> getById(Long id) {
        return _offerSlotRepository.findById(id).map(_OfferSlotMapper :: toDto);
    }
    @Override
    public OfferSlotDto create(OfferSlotDto dto) {
        OfferSlot offerEntity = _OfferSlotMapper.toEntity(dto);
        ChargingStation station = _chargingStationRepository.findById(dto.getStationId())
                        .orElseThrow(() -> new RuntimeException("Station not found with id " + dto.getStationId()));
        offerEntity.setChargingStation(station);
        var savedOffer = _offerSlotRepository.save(offerEntity);  
        var savedOfferDto = _OfferSlotMapper.toDto(savedOffer);
        savedOfferDto.setStationId(dto.getStationId());
        return savedOfferDto;
    }
    @Override
    public OfferSlotDto update(Long id, OfferSlotDto dto) {
        var offerSlot = _offerSlotRepository.findById(id).get();
        if(offerSlot != null){
            OfferSlot offer = _OfferSlotMapper.toEntity(dto);
            offerSlot.setTimeSlot(offer.getTimeSlot());
            offerSlot.setPricePerSlot(offer.getPricePerSlot());
            offerSlot.setIsAvailable(offer.getIsAvailable());
            offerSlot.setSlotDate(offer.getSlotDate());
            OfferSlot savedOffer = _offerSlotRepository.save(offer);
            var offerslotDto = _OfferSlotMapper.toDto(savedOffer);
            offerslotDto.setStationId(dto.getStationId());
            return offerslotDto;
        }
        return null;
    }
    @Override
    public boolean delete(Long id) {
        if(_offerSlotRepository.existsById(id)){
            _offerSlotRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
