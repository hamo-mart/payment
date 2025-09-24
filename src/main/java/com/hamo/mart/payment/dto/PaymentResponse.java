package com.hamo.mart.payment.dto;

import com.hamo.mart.payment.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponse {

    private String orderId;
    private Status status;
    private ZonedDateTime approvedAt;

}
