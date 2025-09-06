package com.sa.client_service.shared.application.outputports;

import java.util.Optional;

import com.sa.client_service.shared.application.dtos.CreatePaymentDTO;

public interface CreatePaymentOutputPort {
    public boolean createPayment(CreatePaymentDTO createPaymentDTO);
}
