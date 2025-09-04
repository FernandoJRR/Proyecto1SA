package com.sa.finances_service.payments.application.inputports;

import com.sa.finances_service.payments.domain.Payment;
import com.sa.shared.exceptions.NotFoundException;

public interface FindPaymentByIdInputPort {
    public Payment handle(String id) throws NotFoundException;
}
