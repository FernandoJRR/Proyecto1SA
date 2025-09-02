package com.sa.establishment_service.restaurants.application.inputports;

import java.util.UUID;

import com.sa.establishment_service.restaurants.application.dtos.CreateDishDTO;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreateDishInputPort {
    public Dish handle(String restaurantId, @Valid CreateDishDTO createDishDTO) throws NotFoundException;
}
