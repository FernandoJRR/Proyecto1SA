package com.sa.finances_service.reports.application.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OutcomeDTO {
    private BigDecimal totalOutcomeHotels;
    private BigDecimal totalOutcomeRestaurants;
}
