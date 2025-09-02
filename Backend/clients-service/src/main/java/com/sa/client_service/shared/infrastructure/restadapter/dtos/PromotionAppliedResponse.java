package com.sa.client_service.shared.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

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
