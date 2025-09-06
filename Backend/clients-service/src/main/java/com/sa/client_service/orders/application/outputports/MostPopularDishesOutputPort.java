package com.sa.client_service.orders.application.outputports;

import java.util.List;
import java.util.UUID;

public interface MostPopularDishesOutputPort {
    public List<UUID> mostPopularDishes(UUID restaurantId);
}
