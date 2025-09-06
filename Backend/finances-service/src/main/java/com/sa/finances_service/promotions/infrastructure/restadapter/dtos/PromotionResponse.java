package com.sa.finances_service.promotions.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.finances_service.promotions.domain.PromotionType.PromotionTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private UUID id;
    private BigDecimal percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private PromotionTypeInfo promotionType;
    private String name;
    private UUID establishmentId;
    private String establishmentType;
}
