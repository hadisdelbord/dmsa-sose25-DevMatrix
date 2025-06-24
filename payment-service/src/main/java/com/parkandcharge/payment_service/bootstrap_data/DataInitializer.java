package com.parkandcharge.payment_service.bootstrap_data;

import com.parkandcharge.payment_service.model.Payment;
import com.parkandcharge.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void run(String... args) throws Exception {
        paymentRepository.deleteAll();

        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        Payment payment1 = new Payment();
        payment1.setBookingId(1001L);
        payment1.setBookingAmount(150.0f);
        payment1.setBookingDate(currentDate);
        payment1.setPaymentMethod("Credit Card");

        Payment payment2 = new Payment();
        payment2.setBookingId(1002L);
        payment2.setBookingAmount(200.5f);
        payment2.setBookingDate(currentDate);
        payment2.setPaymentMethod("PayPal");

        Payment payment3 = new Payment();
        payment3.setBookingId(1001L);
        payment3.setBookingAmount(99.99f);
        payment3.setBookingDate(currentDate);
        payment3.setPaymentMethod("Cash");

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        System.out.println("Sample payment data initialized.");
    }
}
