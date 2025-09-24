package com.hamo.mart.payment.service.stategy;

import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;

import com.hamo.mart.payment.dto.TossRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentProcessor {

    private final Map<String, PaymentStrategy> strategies;


    public PaymentProcessor(List<PaymentStrategy> paymentStrategies){
        this.strategies = paymentStrategies.stream()
                .collect(Collectors.toMap(s-> s.getClass().getSimpleName(), s->s));
    }

    public Mono<PaymentResponse> process(String type, PaymentRegisterRequest request){
        PaymentStrategy strategy = strategies.getOrDefault(type, null);
        if(strategy == null){
            throw new IllegalArgumentException("No strategy found for type: " + type);
        }
        return strategy.confirmPayment(new TossRequest(request.getPaymentKey(), request.getOrderId(), request.getAmount()));
    }
}
