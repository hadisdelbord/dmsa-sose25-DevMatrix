package com.DevMatrix.StationManagementService.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.DevMatrix.StationManagementService.domain.entity.ChargingStation;

@Repository
public interface ChargingStationRepository extends CrudRepository<ChargingStation, Long> {

    
}
