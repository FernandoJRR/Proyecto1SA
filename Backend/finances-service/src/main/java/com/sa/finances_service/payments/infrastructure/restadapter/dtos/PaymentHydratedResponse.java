package com.sa.finances_service.payments.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.finances_service.payments.application.dtos.HotelDTO;
import com.sa.finances_service.payments.application.dtos.RestaurantDTO;
import com.sa.finances_service.shared.infrastructure.dtos.PromotionAppliedResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentHydratedResponse {
    private String id;
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

    private HotelDTO hotel;
    private RestaurantDTO restaurant;
}
