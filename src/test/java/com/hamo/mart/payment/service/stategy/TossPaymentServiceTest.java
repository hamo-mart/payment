package com.hamo.mart.payment.service.stategy;

import com.hamo.mart.payment.dto.PaymentRegisterRequest;
import com.hamo.mart.payment.dto.PaymentResponse;
import com.hamo.mart.payment.dto.TossRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class TossPaymentServiceTest {

    @Test
    void confirmPayment_mockCall() {
        // given
        TossRequest tossRequest = new TossRequest("pay_mockKey", "ORD-1234", 1000);

        // 가짜 응답 JSON
        String mockJson = "{ \"orderId\": \"ORD-1234\", \"status\": \"READY\" }";

        // WebClient Mock 구성
        ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
        ClientResponse clientResponse = ClientResponse.create(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(mockJson)
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(clientResponse));

        WebClient mockWebClient = WebClient.builder()
                .baseUrl("https://api.tosspayments.com/v1/payments")
                .exchangeFunction(exchangeFunction)
                .build();

        TossPaymentService tossPaymentService = new TossPaymentService(mockWebClient.mutate(), "test_secret");

        // when
        Mono<PaymentResponse> result = tossPaymentService.confirmPayment(tossRequest);

        StepVerifier.create(result)
                .expectNextMatches(res ->
                        "ORD-1234".equals(res.getOrderId()) &&
                                res.getStatus() != null
                )
                .verifyComplete();

    }
}
