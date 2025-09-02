package com.sa.finances_service.payments.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.finances_service.shared.infrastructure.dtos.PromotionAppliedResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
