package com.sa.establishment_service.restaurants.application.inputports;

import com.sa.establishment_service.restaurants.application.dtos.UpdateDishDTO;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface UpdateDishInputPort {
    public Dish handle(String restaurantId, String dishId, @Valid UpdateDishDTO updateDishDTO) throws NotFoundException;
}

