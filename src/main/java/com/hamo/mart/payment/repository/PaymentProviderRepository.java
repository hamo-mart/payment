package com.hamo.mart.payment.repository;

import com.hamo.mart.payment.domain.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {
}
