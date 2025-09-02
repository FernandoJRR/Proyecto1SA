package com.sa.client_service.orders.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends Auditor {
    private UUID dishId;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public OrderItem(UUID id, String name, UUID dishId, Integer quantity, BigDecimal price) {
        super(id);
        this.name = name;
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem create(UUID dishId, String name, Integer quantity, BigDecimal price) {
        return new OrderItem(UUID.randomUUID(), name, dishId, quantity, price);
    }
}
