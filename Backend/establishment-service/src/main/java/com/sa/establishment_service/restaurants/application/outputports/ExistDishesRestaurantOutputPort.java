package com.sa.establishment_service.restaurants.application.outputports;

import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Dish;

public interface ExistDishesRestaurantOutputPort {
    public List<Dish> findDishesByRestaurantAndIds(String restaurantId, List<String> presentIds);
}
