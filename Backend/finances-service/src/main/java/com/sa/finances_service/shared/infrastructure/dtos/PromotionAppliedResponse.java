package com.sa.finances_service.shared.infrastructure.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PromotionAppliedResponse {
    private UUID promotionId;
    private String name;
    private String type;
    private BigDecimal amountOff;
    private BigDecimal percentOff;
}
