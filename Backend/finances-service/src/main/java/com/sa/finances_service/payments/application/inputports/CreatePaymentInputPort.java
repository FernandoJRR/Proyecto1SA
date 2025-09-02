package com.sa.finances_service.payments.application.inputports;

import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.shared.exceptions.NotFoundException;

public interface CreatePaymentInputPort {
    public Payment handle(CreatePaymentDTO createPaymentDTO) throws NotFoundException;
}
