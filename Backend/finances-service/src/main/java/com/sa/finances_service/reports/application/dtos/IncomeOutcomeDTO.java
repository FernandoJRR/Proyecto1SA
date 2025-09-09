package com.sa.finances_service.reports.application.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.sa.finances_service.payments.domain.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncomeOutcomeDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalOutcome;
    private OutcomeDTO outcome;
    private List<Payment> payments;
}
