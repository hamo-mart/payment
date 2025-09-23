package com.hamo.mart.payment.service;

import com.hamo.mart.payment.domain.Payment;
import com.hamo.mart.payment.domain.PaymentProvider;
import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;
import com.hamo.mart.payment.exception.PaymentProviderNotFoundException;
import com.hamo.mart.payment.repository.PaymentProviderRepository;
import com.hamo.mart.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProviderRepository paymentProviderRepository;

    @Transactional
    @Override
    public PaymentResponse processPayment(PaymentRegisterRequest request) {
        PaymentProvider paymentProvider = paymentProviderRepository
                .findById(request.getPayment_provider_id())
                .orElseThrow(() -> new PaymentProviderNotFoundException(request.getPayment_provider_id()));

        Payment payment = Payment
                .builder()
                .type(request.getType())
                .method(request.getMethod())
                .status(request.getStatus())
                .orderNumber(request.getOrderNumber())
                .totalAmount(request.getTotalAmount())
                .paymentProvider(paymentProvider)
                .build();
        Payment save = paymentRepository.save(payment);
        return toPaymentResponse(save, paymentProvider.getName());
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
                payment.getId(),
                payment.getOrderNumber(),
                payment.getTotalAmount(),
                paymentProviderName,
                payment.getType(),
                payment.getMethod(),
                payment.getStatus()
        );
    }
}
