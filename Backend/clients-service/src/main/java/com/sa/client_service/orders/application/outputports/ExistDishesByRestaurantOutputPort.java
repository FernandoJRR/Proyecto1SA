package com.sa.client_service.orders.application.outputports;

import java.util.List;
import java.util.UUID;

import com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO;

public interface ExistDishesByRestaurantOutputPort {
    public ExistantDishesRestaurantDTO existantDishesRestaurant(String restaurantId, List<UUID> dishesId);
}
