package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private String id;
    private String name;
    private BigDecimal price;
}
