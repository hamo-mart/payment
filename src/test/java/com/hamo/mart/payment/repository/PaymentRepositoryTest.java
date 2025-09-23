package com.hamo.mart.payment.repository;

import com.hamo.mart.payment.domain.Method;
import com.hamo.mart.payment.domain.Payment;
import com.hamo.mart.payment.domain.Status;
import com.hamo.mart.payment.domain.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    TestEntityManager testEntityManager;

    @Autowired
    PaymentRepository paymentRepository;


    @Test
    @DisplayName("주문등록")
    void testSavePayment() {
        Payment payment = Payment.builder()
                .orderNumber("ORD123456")
                .type(Type.CARD)
                .method(Method.CREDIT_CARD)
                .status(Status.READY)
                .totalAmount(1000)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        assertNotNull(savedPayment.getId());
        assertEquals("ORD123456", savedPayment.getOrderNumber());
        assertEquals(Type.CARD, savedPayment.getType());
        assertEquals(Method.CREDIT_CARD, savedPayment.getMethod());
        assertEquals(Status.READY, savedPayment.getStatus());
        assertEquals(1000, savedPayment.getTotalAmount());
    }

    @Test
    @DisplayName("주문 단일 조회")
    void testFindPaymentById() {
        Payment payment = Payment.builder()
                .orderNumber("ORD123456")
                .type(Type.CARD)
                .method(Method.CREDIT_CARD)
                .status(Status.READY)
                .totalAmount(1000)
                .build();
        Payment savedPayment = paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

        assertNotNull(foundPayment);
        assertEquals(savedPayment.getId(), foundPayment.getId());
    }

    @Test
    @DisplayName("주문 목록 조회")
    void testFindAllPayments() {
        Payment payment1 = Payment.builder()
                .orderNumber("ORD123456")
                .type(Type.CARD)
                .method(Method.CREDIT_CARD)
                .status(Status.READY)
                .totalAmount(1000)
                .build();
        Payment payment2 = Payment.builder()
                .orderNumber("ORD123456")
                .type(Type.CARD)
                .method(Method.MOBILE_PAYMENT)
                .status(Status.COMPLETED)
                .totalAmount(2000)
                .build();
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        Pageable pageable = PageRequest.of(0, 2);

        Page<Payment> payments = paymentRepository.findAllByOrderNumber("ORD123456", pageable);

        assertNotNull(payments);
        assertEquals(2, payments.getTotalElements());
        assertEquals(1, payments.getTotalPages());
        assertEquals(2, payments.getContent().size());
        assertEquals("ORD123456", payments.getContent().get(0).getOrderNumber());
    }

}