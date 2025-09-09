package com.sa.finances_service.payments.application.inputports;

import java.time.LocalDate;

import com.sa.finances_service.reports.application.dtos.IncomeOutcomeDTO;
import com.sa.shared.exceptions.NotFoundException;

public interface GetIncomeOutcomeInputPort {
    public IncomeOutcomeDTO handle(LocalDate fromDate, LocalDate toDate)
        throws NotFoundException;
}
