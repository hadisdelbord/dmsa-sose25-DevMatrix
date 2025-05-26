package com.parkandcharge.booking_service.repository;

import com.parkandcharge.booking_service.model.Booking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // get Booking By User ID
    List<Booking> findByUserId(Long userId);

}
