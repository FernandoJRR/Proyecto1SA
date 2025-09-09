package com.sa.finances_service.reports.infrastructure.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutcomeResponse {
    private BigDecimal totalOutcomeHotels;
    private BigDecimal totalOutcomeRestaurants;
}
