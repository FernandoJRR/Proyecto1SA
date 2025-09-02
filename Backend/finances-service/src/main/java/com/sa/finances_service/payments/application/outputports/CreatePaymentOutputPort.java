package com.sa.finances_service.payments.application.outputports;

import com.sa.finances_service.payments.domain.Payment;

public interface CreatePaymentOutputPort {
    public Payment createPayment(Payment payment);
}
