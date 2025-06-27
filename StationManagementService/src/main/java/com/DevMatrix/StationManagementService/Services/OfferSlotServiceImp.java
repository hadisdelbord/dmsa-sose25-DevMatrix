package com.DevMatrix.StationManagementService.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevMatrix.StationManagementService.Dtos.AvailableSlotDto;
import com.DevMatrix.StationManagementService.Dtos.OfferAndStationDto;
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

    public void createAll(List<OfferSlotDto> dtos) {
    List<OfferSlot> entities = dtos.stream().map(dto -> {
        OfferSlot slot = new OfferSlot();
        slot.setTimeSlot(dto.getTimeSlot());
        slot.setPricePerSlot(dto.getPricePerSlot());
        slot.setIsAvailable(dto.getIsAvailable());
        slot.setSlotDate(dto.getSlotDate());

        ChargingStation station = new ChargingStation();
        station.setId(dto.getStationId());
        slot.setChargingStation(station);

        return slot;
    }).collect(Collectors.toList());

    _offerSlotRepository.saveAll(entities);
    }

    public void createOrUpdateAll(List<OfferSlotDto> dtos) {
    for (OfferSlotDto dto : dtos) {
        Optional<OfferSlot> existingSlotOpt = _offerSlotRepository
            .findByChargingStationIdAndSlotDateAndTimeSlot(
                dto.getStationId(),
                dto.getSlotDate(),
                dto.getTimeSlot()
            );

        if (existingSlotOpt.isPresent()) {
            OfferSlot existingSlot = existingSlotOpt.get();
            existingSlot.setPricePerSlot(dto.getPricePerSlot());
            existingSlot.setIsAvailable(dto.getIsAvailable());
            _offerSlotRepository.save(existingSlot);
        } else {
            OfferSlot slot = new OfferSlot();
            slot.setTimeSlot(dto.getTimeSlot());
            slot.setPricePerSlot(dto.getPricePerSlot());
            slot.setIsAvailable(dto.getIsAvailable());
            slot.setSlotDate(dto.getSlotDate());
            slot.setSlotDuration(dto.getSlotDuration());

            ChargingStation station = new ChargingStation();
            station.setId(dto.getStationId());
            slot.setChargingStation(station);

            _offerSlotRepository.save(slot);
        }
    }
}

 @Override
    public OfferSlotDto update(Long id, OfferSlotDto dto) {
        var offerSlotOpt = _offerSlotRepository.findById(id);
        if (offerSlotOpt.isPresent()) {
            OfferSlot offerSlot = offerSlotOpt.get();

            // Only update if dto has non-null values
            if (dto.getIsAvailable() != null) {
                offerSlot.setIsAvailable(dto.getIsAvailable());
            }

            // Uncomment if you want to allow updating these fields explicitly
            if (dto.getTimeSlot() != null) {
                offerSlot.setTimeSlot(dto.getTimeSlot());
            }
            if (dto.getPricePerSlot() != null) {
                offerSlot.setPricePerSlot(dto.getPricePerSlot());
            }
            if (dto.getSlotDate() != null) {
                offerSlot.setSlotDate(dto.getSlotDate());
            }

            OfferSlot savedOffer = _offerSlotRepository.save(offerSlot);
            OfferSlotDto offerslotDto = _OfferSlotMapper.toDto(savedOffer);
            offerslotDto.setStationId(dto.getStationId()); // if needed
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

    public List<AvailableSlotDto> GetAvailableOffers(String postalCode){
        var availableOffer = _offerSlotRepository.GetAvailableOffers(postalCode);
        var offerslotDto = _OfferSlotMapper.toDtoListWithAddress(availableOffer);
        return offerslotDto;
    }

     public Optional<OfferAndStationDto> GetOfferWithStationById(long offerId){
        var offerslotDto = _offerSlotRepository.findById(offerId).map(_OfferSlotMapper :: toDtoWithStation);;
        return offerslotDto;
    }
    
    public List<OfferSlotDto> GetByStationIdAndDate(Long stationId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); 
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            List<OfferSlot> slots = _offerSlotRepository.findByChargingStationIdAndSlotDateBetween(
        stationId,startOfDay, endOfDay
    );
            return slots.stream()
                .map(_OfferSlotMapper::toDto)
                .collect(Collectors.toList());
    }

}
