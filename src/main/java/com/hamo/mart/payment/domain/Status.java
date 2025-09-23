package com.hamo.mart.payment.domain;

public enum Status {

    READY, // 결제 준비
    IN_PROGRESS, // 결제 진행 중
    COMPLETED, // 결제 완료
    FAILED, // 결제 실패
    CANCELLED // 결제 취소
}
