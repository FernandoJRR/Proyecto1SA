package com.sa.client_service.orders.infrastructure.restadapter.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequest {
    private String clientCui;

    private UUID restaurantId;

    private UUID promotionId;

    private List<CreateOrderItemRequest> createOrderItemRequests;
}
