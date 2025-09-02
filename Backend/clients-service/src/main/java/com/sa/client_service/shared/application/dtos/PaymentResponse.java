package com.sa.client_service.shared.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.client_service.shared.infrastructure.restadapter.dtos.PromotionAppliedResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PaymentResponse {
    private UUID establishmentId;
    private UUID clientId;
    private String sourceType;
    private UUID sourceId;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private String method;
    private String status;

    private String cardNumber;

    private PromotionAppliedResponse promotionApplied;
}
