package com.DevMatrix.StationManagementService.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.DevMatrix.StationManagementService.domain.entity.OfferSlot;

@Repository
public interface OfferSlotRepository extends CrudRepository<OfferSlot, Long> {
    
}
