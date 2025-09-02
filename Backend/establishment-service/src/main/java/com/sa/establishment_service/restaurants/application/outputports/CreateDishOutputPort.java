package com.sa.establishment_service.restaurants.application.outputports;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

public interface CreateDishOutputPort {
    public Dish createDish(String restaurantId, Dish dish) throws NotFoundException;
}
