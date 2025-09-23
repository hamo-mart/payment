package com.hamo.mart.payment.repository;

import com.hamo.mart.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"paymentProvider"})
    Optional<Payment> findWithPaymentProviderById(Long id);

    @EntityGraph(attributePaths = {"paymentProvider"})
    Page<Payment> findAllByOrderNumber(String orderNumber, Pageable pageable);
}
