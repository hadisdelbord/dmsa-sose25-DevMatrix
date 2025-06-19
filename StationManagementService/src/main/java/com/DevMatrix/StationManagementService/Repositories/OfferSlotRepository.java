package com.DevMatrix.StationManagementService.Repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

@Repository
public interface OfferSlotRepository extends CrudRepository<OfferSlot, Long> {

    
    @Query("SELECT o FROM OfferSlot o JOIN FETCH o.chargingStation cs JOIN FETCH cs.address WHERE o.isAvailable = true")
    List<OfferSlot> GetAvailableOffers();
    List<OfferSlot> findByChargingStationIdAndSlotDateBetween(long stationId, LocalDateTime startOfDay,LocalDateTime endOfDay);
    Optional<OfferSlot> findByChargingStationIdAndSlotDateAndTimeSlot( long stationId, LocalDateTime slotDate  ,BigDecimal timeSlot);
}
