package com.parkandcharge.payment_service.service;

import com.parkandcharge.payment_service.model.Payment;
import com.parkandcharge.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Create new payment
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get payment by id (primary key)
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    // Update payment by id
    public Payment updatePayment(Long id, Payment updatedPayment) {
        return paymentRepository.findById(id).map(payment -> {
            payment.setBookingId(updatedPayment.getBookingId());
            payment.setBookingAmount(updatedPayment.getBookingAmount());
            payment.setBookingDate(updatedPayment.getBookingDate());
            payment.setPaymentMethod(updatedPayment.getPaymentMethod());
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    // Delete payment by id
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // Find payments by bookingId (foreign key)
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}
