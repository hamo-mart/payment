package com.hamo.mart.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Type type;

    private Method method;

    private Status status;

    private String orderNumber;

    private ZonedDateTime approvedAt;

    private Integer totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentProvider paymentProvider;

    @Builder
    public Payment(Type type, Method method, Status status, String orderNumber, ZonedDateTime approvedAt, Integer totalAmount, PaymentProvider paymentProvider) {
        this.type = type;
        this.method = method;
        this.status = status;
        this.orderNumber = orderNumber;
        this.approvedAt = approvedAt;
        this.totalAmount = totalAmount;
        this.paymentProvider = paymentProvider;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }






}
