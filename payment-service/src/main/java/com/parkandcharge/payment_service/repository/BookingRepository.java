package com.parkandcharge.payment_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parkandcharge.payment_service.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // get Booking By User ID
    List<Booking> findByUserId(Long userId);

}
