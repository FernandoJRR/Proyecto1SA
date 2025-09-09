package com.sa.client_service.clients.application.outputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.client_service.clients.application.dtos.PaymentDTO;

public interface FindPaymentsByClientOutputPort {
    public List<PaymentDTO> findByClient(String cui, String establishmentId, LocalDate fromDate, LocalDate toDate);
}
