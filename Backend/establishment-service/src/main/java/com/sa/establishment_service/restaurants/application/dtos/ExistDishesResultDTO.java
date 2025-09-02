package com.sa.establishment_service.restaurants.application.dtos;

import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Dish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExistDishesResultDTO {
    private boolean allPresent;
    private List<UUID> missingIds;
    private List<Dish> presentDishes;
}
