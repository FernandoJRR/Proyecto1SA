package com.sa.finances_service.payments.application.inputports;

import java.util.List;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.domain.Payment;

public interface FindPaymentsInputPort {
    public List<Payment> handle(FindPaymentsDTO filter);
}
