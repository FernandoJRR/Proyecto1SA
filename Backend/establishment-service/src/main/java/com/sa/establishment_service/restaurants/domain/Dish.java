package com.sa.establishment_service.restaurants.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dish extends Auditor {
    private String name;
    private BigDecimal price;

    public Dish(UUID id, String name, BigDecimal price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public static Dish create(String name, BigDecimal price) {
        return new Dish(UUID.randomUUID(), name, price);
    }
}
