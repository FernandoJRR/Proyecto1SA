package com.sa.finances_service.payments.application.outputports;

import java.util.Optional;

import com.sa.finances_service.payments.domain.Payment;

public interface FindPaymentByIdOutputPort {
    public Optional<Payment> findById(String id);
}
