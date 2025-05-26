package com.DevMatrix.StationManagementService.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.DevMatrix.StationManagementService.domain.entity.Address;


@Repository
public interface IAddressRepository extends CrudRepository<Address, Long> {
    Optional<Address> findByPostalCode(String postalCode);
}
