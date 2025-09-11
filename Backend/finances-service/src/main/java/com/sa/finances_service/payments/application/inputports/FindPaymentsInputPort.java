package com.sa.finances_service.payments.application.inputports;

import java.util.List;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.domain.Payment;

import jakarta.validation.Valid;

public interface FindPaymentsInputPort {
    public List<Payment> handle(@Valid FindPaymentsDTO filter);
}
