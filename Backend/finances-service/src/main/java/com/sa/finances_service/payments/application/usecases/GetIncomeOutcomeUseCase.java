package com.sa.finances_service.payments.application.usecases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.application.inputports.GetIncomeOutcomeInputPort;
import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.application.outputports.GetOutcomeByDateOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.reports.application.dtos.IncomeOutcomeDTO;
import com.sa.finances_service.reports.application.dtos.OutcomeDTO;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetIncomeOutcomeUseCase implements GetIncomeOutcomeInputPort {

    private final FindPaymentsOutputPort findPaymentsOutputPort;
    private final GetOutcomeByDateOutputPort getOutcomeByDateOutputPort;

    @Override
    public IncomeOutcomeDTO handle(LocalDate fromDate, LocalDate toDate) throws NotFoundException {

        //TODO: add date validation

        List<Payment> payments = findPaymentsOutputPort.findAll(
            FindPaymentsDTO.builder()
            .fromDate(fromDate)
            .toDate(toDate)
            .build()
        );

        BigDecimal totalIncome = payments.stream()
            .map(Payment::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        OutcomeDTO foundOutcome = getOutcomeByDateOutputPort.getByDate(fromDate, toDate)
            .orElseThrow(() -> new NotFoundException("Los egresos no pudieron obtenerse"));

        BigDecimal totalOutcome = foundOutcome.getTotalOutcomeHotels()
            .add(foundOutcome.getTotalOutcomeRestaurants());

        return new IncomeOutcomeDTO(totalIncome, totalOutcome, foundOutcome, payments);
    }

}
