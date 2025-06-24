package com.parkandcharge.payment_service.repository;

import com.parkandcharge.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find all payments by bookingId
    List<Payment> findByBookingId(Long bookingId);
}
