package com.sa.client_service.orders.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sa.client_service.shared.infrastructure.restadapter.dtos.PromotionAppliedResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderResponse {
    private String clientCui;
    private UUID restaurantId;
    private List<OrderItemResponse> items;
    private BigDecimal total;
    private BigDecimal subtotal;
    private PromotionAppliedResponse promotionApplied;
}
