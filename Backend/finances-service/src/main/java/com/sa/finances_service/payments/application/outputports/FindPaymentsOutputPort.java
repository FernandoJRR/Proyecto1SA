package com.sa.finances_service.payments.application.outputports;

import java.util.List;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.domain.Payment;

public interface FindPaymentsOutputPort {
    public List<Payment> findAll(FindPaymentsDTO dto);
}
