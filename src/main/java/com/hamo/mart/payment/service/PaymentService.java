package com.hamo.mart.payment.service;

import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<PaymentResponse> processPayment(PaymentRegisterRequest request);

    PaymentResponse getPaymentDetails(Long paymentId);

    Page<PaymentResponse> getPaymentsByOrderNumber(String orderNumber, Pageable pageable);
}
