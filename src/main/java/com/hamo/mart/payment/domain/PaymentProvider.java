package com.hamo.mart.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_providers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_provider_id")
    private Long id;

    private String name;


    public PaymentProvider(String name) {
        this.name = name;
    }
}
