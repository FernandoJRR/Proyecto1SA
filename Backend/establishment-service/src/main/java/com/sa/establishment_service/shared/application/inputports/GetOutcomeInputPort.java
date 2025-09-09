package com.sa.establishment_service.shared.application.inputports;

import java.time.LocalDate;

import com.sa.establishment_service.shared.application.dtos.OutcomeDTO;

public interface GetOutcomeInputPort {
    public OutcomeDTO handle(LocalDate fromDate, LocalDate toDate);
}
