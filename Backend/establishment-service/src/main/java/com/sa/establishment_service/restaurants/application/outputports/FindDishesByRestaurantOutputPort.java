package com.sa.establishment_service.restaurants.application.outputports;

import java.util.List;

import com.sa.establishment_service.restaurants.domain.Dish;

public interface FindDishesByRestaurantOutputPort {
    public List<Dish> findByRestaurant(String restaurantId);
}
