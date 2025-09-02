package com.sa.client_service.orders.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DishDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
}
