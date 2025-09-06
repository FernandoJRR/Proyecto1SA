package com.sa.client_service.reservations.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PromotionDTO {
    private UUID id;
    private BigDecimal percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private PromotionTypeDTO promotionType;
}
