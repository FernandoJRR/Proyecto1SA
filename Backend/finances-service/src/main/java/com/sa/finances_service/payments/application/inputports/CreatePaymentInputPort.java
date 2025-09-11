package com.sa.finances_service.payments.application.inputports;

import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreatePaymentInputPort {
    public Payment handle(@Valid CreatePaymentDTO createPaymentDTO) throws NotFoundException;
}
