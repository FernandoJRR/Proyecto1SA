package com.sa.client_service.clients.application.inputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.client_service.clients.application.dtos.PaymentDTO;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

public interface GetIncomeByClientInputPort {
       public List<PaymentDTO> handle(String clientCui, String establishmentId,
            LocalDate fromDate, LocalDate toDate) throws InvalidParameterException, NotFoundException;
}
