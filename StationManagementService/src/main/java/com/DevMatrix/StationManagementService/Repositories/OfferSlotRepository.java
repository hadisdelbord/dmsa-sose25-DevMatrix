package com.DevMatrix.StationManagementService.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

@Repository
public interface OfferSlotRepository extends CrudRepository<OfferSlot, Long> {

    
    @Query("SELECT o FROM OfferSlot o JOIN FETCH o.chargingStation cs JOIN FETCH cs.address WHERE o.isAvailable = true")
    List<OfferSlot> GetAvailableOffers();
}
