package com.hamo.mart.payment.service.stategy;

import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;

import com.hamo.mart.payment.dto.TossRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Service
public class TossPaymentService implements PaymentStrategy {

    private final WebClient webClient;

    public TossPaymentService(WebClient.Builder builder, @Value("${toss.payments.secret-key}") String tossSecretKey) {
        this.webClient = builder
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .defaultHeader(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString(tossSecretKey.getBytes()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Mono<PaymentResponse> confirmPayment(TossRequest tossRequest) {
        return webClient.post()
                .uri("/confirm")
                .bodyValue(tossRequest)
                .retrieve()
                .bodyToMono(PaymentResponse.class);
    }
}
