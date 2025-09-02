package com.sa.client_service.orders.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemResponse {
    private UUID dishId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
