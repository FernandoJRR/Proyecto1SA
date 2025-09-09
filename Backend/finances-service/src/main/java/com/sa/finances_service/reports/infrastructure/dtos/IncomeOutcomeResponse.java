package com.sa.finances_service.reports.infrastructure.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.sa.finances_service.payments.infrastructure.restadapter.dtos.PaymentResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncomeOutcomeResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalOutcome;
    private OutcomeResponse outcome;
    private List<PaymentResponse> payments;
}
