package com.hamo.mart.payment.service;

import com.hamo.mart.payment.domain.*;
import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;
import com.hamo.mart.payment.exception.PaymentProviderNotFoundException;
import com.hamo.mart.payment.repository.PaymentProviderRepository;
import com.hamo.mart.payment.repository.PaymentRepository;
import com.hamo.mart.payment.service.stategy.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProviderRepository paymentProviderRepository;

    private final PaymentProcessor paymentProcessor;

    @Transactional
    @Override
    public Mono<PaymentResponse> processPayment(PaymentRegisterRequest request) {

        return paymentProcessor.process(request.getStrategy(), request)
                .publishOn(Schedulers.boundedElastic()) // 블로킹 전용 스레드 풀로 전환
                .map(response -> {
                    PaymentProvider provider = paymentProviderRepository
                            .findById(request.getProviderId())
                            .orElseThrow(() -> new PaymentProviderNotFoundException(request.getProviderId()));

                    Payment payment = Payment.builder()
                            .orderNumber(request.getOrderNumber())
                            .totalAmount(request.getAmount())
                            .type(Type.TOSS)
                            .method(Method.BANK_TRANSFER)
                            .status(Status.READY)
                            .paymentProvider(provider)
                            .build();

                    paymentRepository.save(payment); // 블로킹 DB 저장
                    return response;
                });
    }

    @Override
    public PaymentResponse getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findWithPaymentProviderById(paymentId).orElseThrow(
                () -> new PaymentProviderNotFoundException(paymentId)
        );
        return toPaymentResponse(payment, payment.getPaymentProvider().getName());
    }

    @Override
    public Page<PaymentResponse> getPaymentsByOrderNumber(String orderNumber, Pageable pageable) {
        return paymentRepository.findAllByOrderNumber(orderNumber, pageable)
                .map(payment -> toPaymentResponse(payment, payment.getPaymentProvider().getName()));
    }

    private PaymentResponse toPaymentResponse(Payment payment, String paymentProviderName) {
        return new PaymentResponse(
                payment.getOrderNumber(),
                payment.getStatus(),
                payment.getApprovedAt()
        );
    }
}
