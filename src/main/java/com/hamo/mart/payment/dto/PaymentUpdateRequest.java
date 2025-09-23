package com.hamo.mart.payment.dto;

import com.hamo.mart.payment.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentUpdateRequest {

    private Status status;
}
