package com.sa.establishment_service.shared.application.inputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.establishment_service.shared.application.dtos.PaymentDTO;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

public interface GetIncomeEstablishmentInputPort {
    public List<PaymentDTO> handle(String establishmentId, String establishmentType, LocalDate fromDate, LocalDate toDate) throws InvalidParameterException, NotFoundException;
}
