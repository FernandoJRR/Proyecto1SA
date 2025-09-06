package com.sa.finances_service.promotions.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePromotionRequest {

    private BigDecimal percentage;

    private LocalDate startDate;

    private LocalDate endDate;

    private String promotionType;

    private String establishmentId;

    private String establishmentType;

    private String name;

    private Integer topCount;
}
