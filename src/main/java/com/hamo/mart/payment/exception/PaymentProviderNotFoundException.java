package com.hamo.mart.payment.exception;

public class PaymentProviderNotFoundException extends RuntimeException {
    public PaymentProviderNotFoundException(String message) {
        super(message);
    }

    public PaymentProviderNotFoundException(Long id) {
        super("Payment provider not found with id: " + id);
    }
}
