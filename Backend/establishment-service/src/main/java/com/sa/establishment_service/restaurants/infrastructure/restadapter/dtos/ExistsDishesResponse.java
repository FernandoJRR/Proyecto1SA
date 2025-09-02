package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Dish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExistsDishesResponse {
    private boolean allPresent;
    private List<UUID> missingIds;
    private List<DishResponse> presentDishes;
}
