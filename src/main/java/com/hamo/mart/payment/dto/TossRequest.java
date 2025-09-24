package com.hamo.mart.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TossRequest {

    private String paymentKey;
    private String orderId;
    private Integer amount;
}
