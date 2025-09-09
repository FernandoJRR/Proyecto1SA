package com.sa.establishment_service.shared.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OutcomeResponse {
    private BigDecimal totalOutcomeHotels;
    private BigDecimal totalOutcomeRestaurants;
}
