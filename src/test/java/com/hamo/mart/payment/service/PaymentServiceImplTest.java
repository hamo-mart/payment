package com.hamo.mart.payment.service;

import com.hamo.mart.payment.domain.*;
import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;
import com.hamo.mart.payment.repository.PaymentProviderRepository;
import com.hamo.mart.payment.repository.PaymentRepository;
import com.hamo.mart.payment.service.stategy.PaymentProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {


    @Mock
    private PaymentProviderRepository paymentProviderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentProcessor paymentProcessor;


    @InjectMocks
    private PaymentServiceImpl paymentService;


    @Test
    @DisplayName("결제 서비스")
    void testPaymentService() {


        PaymentRegisterRequest request =
                new PaymentRegisterRequest(
                        "Toss",
                        "38831811234567",
                        "CARD",
                        "CREDIT_CARD",
                        1110
        );

        Payment payment = Payment
                .builder()
                .orderNumber(request.getOrderNumber())
                .totalAmount(request.getAmount())
                .build();
        PaymentProvider paymentProvider = new PaymentProvider("KAKAO");
        ReflectionTestUtils.setField(paymentProvider, "id", 1L);

        when(paymentProcessor.process(anyString(), any(PaymentRegisterRequest.class)))
                .thenReturn(Mono.just(new PaymentResponse("APPROVED", Status.READY, ZonedDateTime.now())));
        when(paymentProviderRepository.findById(any())).thenReturn(Optional.of(paymentProvider));
        when(paymentRepository.save(any())).thenReturn(payment);

        PaymentResponse paymentResponse = paymentService.processPayment(request).block();

        assertNotNull(paymentResponse);

    }

    @Test
    @DisplayName("결제 단일 조회")
    void testGetPaymentDetails() {
        Long paymentId = 1L;
        Payment payment = Payment
                .builder()
                .type(Type.CARD)
                .method(Method.CREDIT_CARD)
                .status(Status.READY)
                .orderNumber("38831811234567")
                .totalAmount(1000)
                .build();
        PaymentProvider paymentProvider = new PaymentProvider("KAKAO");
        ReflectionTestUtils.setField(paymentProvider, "id", 1L);
        ReflectionTestUtils.setField(payment, "id", paymentId);
        ReflectionTestUtils.setField(payment, "paymentProvider", paymentProvider);

        when(paymentRepository.findWithPaymentProviderById(paymentId)).thenReturn(Optional.of(payment));

        PaymentResponse paymentResponse = paymentService.getPaymentDetails(paymentId);

        assertNotNull(paymentResponse);
        assertEquals(payment.getStatus(), paymentResponse.getStatus());
    }

    @Test
    @DisplayName("결제 페이징 조회")
    void testGetPaymentsByOrderNumber() {
        Payment payment1 = Payment
                .builder()
                .type(Type.CARD)
                .method(Method.CREDIT_CARD)
                .status(Status.READY)
                .orderNumber("38831811234567")
                .totalAmount(1000)
                .build();
        Payment payment2 = Payment
                .builder()
                .type(Type.CARD)
                .method(Method.MOBILE_PAYMENT)
                .status(Status.COMPLETED)
                .orderNumber("38831811234567")
                .totalAmount(2000)
                .build();
        PaymentProvider paymentProvider = new PaymentProvider("KAKAO");
        ReflectionTestUtils.setField(paymentProvider, "id", 1L);
        ReflectionTestUtils.setField(payment1, "id", 1L);
        ReflectionTestUtils.setField(payment1, "paymentProvider", paymentProvider);
        ReflectionTestUtils.setField(payment2, "id", 2L);
        ReflectionTestUtils.setField(payment2, "paymentProvider", paymentProvider);

        String orderNumber = "38831811234567";
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        var paymentPage = new org.springframework.data.domain.PageImpl<>(java.util.List.of(payment1, payment2), pageable, 2);

        when(paymentRepository.findAllByOrderNumber(orderNumber, pageable)).thenReturn(paymentPage);

        var paymentResponses = paymentService.getPaymentsByOrderNumber(orderNumber, pageable);
        assertNotNull(paymentResponses);
        assertEquals(2, paymentResponses.getTotalElements());

        assertEquals(payment1.getStatus(), paymentResponses.getContent().get(0).getStatus());
        assertEquals(payment2.getStatus(), paymentResponses.getContent().get(1).getStatus());
    }

}