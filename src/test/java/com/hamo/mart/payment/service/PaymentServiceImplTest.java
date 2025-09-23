package com.hamo.mart.payment.service;

import com.hamo.mart.payment.domain.*;
import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;
import com.hamo.mart.payment.repository.PaymentProviderRepository;
import com.hamo.mart.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {


    @Mock
    private PaymentProviderRepository paymentProviderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;


    @Test
    @DisplayName("결제 서비스")
    void testPaymentService() {


        PaymentRegisterRequest request =
                new PaymentRegisterRequest(
                Type.CARD,
                Method.CREDIT_CARD,
                Status.READY,
                "38831811234567",
                1000,
                1L
        );

        Payment payment = Payment
                .builder()
                .type(request.getType())
                .method(request.getMethod())
                .status(request.getStatus())
                .orderNumber(request.getOrderNumber())
                .totalAmount(request.getTotalAmount())
                .build();
        PaymentProvider paymentProvider = new PaymentProvider("KAKAO");
        ReflectionTestUtils.setField(paymentProvider, "id", 1L);

        when(paymentProviderRepository.findById(any())).thenReturn(Optional.of(paymentProvider));
        when(paymentRepository.save(any())).thenReturn(payment);

        PaymentResponse paymentResponse = paymentService.processPayment(request);

        assertNotNull(paymentResponse);
        assertEquals(request.getOrderNumber(), paymentResponse.getOrderNumber());
        assertEquals(request.getTotalAmount(), paymentResponse.getTotalAmount());
        assertEquals(paymentProvider.getName(), paymentResponse.getPaymentProviderName());
        assertEquals(request.getType(), paymentResponse.getType());
        assertEquals(request.getMethod(), paymentResponse.getMethod());
        assertEquals(request.getStatus(), paymentResponse.getStatus());
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
        assertEquals(payment.getOrderNumber(), paymentResponse.getOrderNumber());
        assertEquals(payment.getTotalAmount(), paymentResponse.getTotalAmount());
        assertEquals(paymentProvider.getName(), paymentResponse.getPaymentProviderName());
        assertEquals(payment.getType(), paymentResponse.getType());
        assertEquals(payment.getMethod(), paymentResponse.getMethod());
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
        assertEquals(1L, paymentResponses.getContent().get(0).getId());
        assertEquals(2L, paymentResponses.getContent().get(1).getId());
        assertEquals(orderNumber, paymentResponses.getContent().get(0).getOrderNumber());
        assertEquals(orderNumber, paymentResponses.getContent().get(1).getOrderNumber());
        assertEquals(paymentProvider.getName(), paymentResponses.getContent().get(0).getPaymentProviderName());
        assertEquals(paymentProvider.getName(), paymentResponses.getContent().get(1).getPaymentProviderName());
        assertEquals(payment1.getType(), paymentResponses.getContent().get(0).getType());
        assertEquals(payment2.getType(), paymentResponses.getContent().get(1).getType());
        assertEquals(payment1.getMethod(), paymentResponses.getContent().get(0).getMethod());
        assertEquals(payment2.getMethod(), paymentResponses.getContent().get(1).getMethod());
        assertEquals(payment1.getStatus(), paymentResponses.getContent().get(0).getStatus());
        assertEquals(payment2.getStatus(), paymentResponses.getContent().get(1).getStatus());
    }

}