package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateDishRequest {
    private String name;
    private BigDecimal price;
}

