package com.sa.client_service.shared.domain;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PromotionApplied {
    private UUID promotionId;
    private String name;
    private BigDecimal amountOff;
    private BigDecimal percentOff;
}
