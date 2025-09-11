package com.sa.client_service.orders.application.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExistantDishesRestaurantDTO {
    private boolean allPresent;
    private List<UUID> missingIds;
    private List<DishDTO> presentDishes;
}
