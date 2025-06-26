package com.DevMatrix.StationManagementService.Repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

@Repository
public interface OfferSlotRepository extends CrudRepository<OfferSlot, Long> {

    @Query("SELECT o FROM OfferSlot o JOIN FETCH o.chargingStation cs JOIN FETCH cs.address a WHERE o.isAvailable = true AND a.postalCode.code = :postalcode")
    List<OfferSlot> GetAvailableOffers(String postalcode);

    List<OfferSlot> findByChargingStationIdAndSlotDateBetween(long stationId, LocalDateTime startOfDay,
            LocalDateTime endOfDay);

    Optional<OfferSlot> findByChargingStationIdAndSlotDateAndTimeSlot(long stationId, LocalDateTime slotDate,
            String timeSlot);

    // getting Sataions and offerslots data by offer_id
    @Query("""
                SELECT o
                FROM OfferSlot o
                JOIN FETCH o.chargingStation cs
                WHERE o.id = :offerId
            """)
    Optional<OfferSlot> GetOfferSlotsWithStationById(@Param("offerId") Long offerId);

}
