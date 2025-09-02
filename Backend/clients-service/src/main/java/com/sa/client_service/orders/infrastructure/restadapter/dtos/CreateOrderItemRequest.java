package com.sa.client_service.orders.infrastructure.restadapter.dtos;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateOrderItemRequest {
    private UUID dishId;

    private Integer quantity;
}
