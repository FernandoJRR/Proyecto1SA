package com.sa.establishment_service.shared.application.outputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.establishment_service.shared.application.dtos.PaymentDTO;

public interface FindPaymentsByEstablishmentOutputPort {
    public List<PaymentDTO> findByEstablishment(String id, LocalDate fromDate, LocalDate toDate);
}
