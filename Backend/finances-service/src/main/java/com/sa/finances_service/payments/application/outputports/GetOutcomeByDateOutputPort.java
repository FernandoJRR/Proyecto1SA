package com.sa.finances_service.payments.application.outputports;

import java.time.LocalDate;
import java.util.Optional;

import com.sa.finances_service.reports.application.dtos.OutcomeDTO;

public interface GetOutcomeByDateOutputPort {
    public Optional<OutcomeDTO> getByDate(LocalDate fromDate, LocalDate toDate);
}
