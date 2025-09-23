package com.hamo.mart.payment.service;

import com.hamo.mart.payment.repository.PaymentProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private final PaymentProviderRepository paymentProviderRepository;


}
