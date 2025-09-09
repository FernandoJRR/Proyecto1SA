package com.sa.establishment_service.restaurants.application.inputports;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

public interface FindDishByIdInputPort {
    public Dish handle(String restaurantId, String dishId) throws NotFoundException;
}
