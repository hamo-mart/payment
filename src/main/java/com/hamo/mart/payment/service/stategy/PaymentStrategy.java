package com.hamo.mart.payment.service.stategy;

import com.hamo.mart.payment.dto.PaymentRegisterRequest;

import com.hamo.mart.payment.dto.PaymentResponse;
import com.hamo.mart.payment.dto.TossRequest;
import reactor.core.publisher.Mono;

public interface PaymentStrategy {

    Mono<PaymentResponse> confirmPayment(TossRequest request);
}
