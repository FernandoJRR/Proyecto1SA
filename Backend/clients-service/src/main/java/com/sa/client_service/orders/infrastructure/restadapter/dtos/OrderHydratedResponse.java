package com.sa.client_service.orders.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sa.client_service.clients.infrastructure.restadapter.dtos.ClientResponse;
import com.sa.client_service.orders.application.dtos.RestaurantDTO;
import com.sa.client_service.shared.infrastructure.restadapter.dtos.PromotionAppliedResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderHydratedResponse {
    private String id;
    private String clientCui;
    private ClientResponse client;
    private UUID restaurantId;
    private RestaurantDTO restaurant;
    private List<OrderItemResponse> items;
    private BigDecimal total;
    private BigDecimal subtotal;
    private PromotionAppliedResponse promotionApplied;
}
