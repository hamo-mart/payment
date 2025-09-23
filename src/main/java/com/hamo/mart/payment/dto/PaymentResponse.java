package com.hamo.mart.payment.dto;

import com.hamo.mart.payment.domain.Method;
import com.hamo.mart.payment.domain.Status;
import com.hamo.mart.payment.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponse {

    private Long id;

    private String orderNumber;

    private Integer totalAmount;

    private String paymentProviderName;

    private Type type;

    private Method method;

    private Status status;

}
