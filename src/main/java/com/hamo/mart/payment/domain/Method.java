package com.hamo.mart.payment.domain;

public enum Method {

    CREDIT_CARD, // 신용카드
    DEBIT_CARD, // 직불카드
    BANK_TRANSFER, // 은행 이체
    MOBILE_PAYMENT, // 휴대폰 결제
    DIGITAL_WALLET, // 디지털 지갑 (예: PayPal, Apple Pay)
    CRYPTOCURRENCY // 암호화폐
}
