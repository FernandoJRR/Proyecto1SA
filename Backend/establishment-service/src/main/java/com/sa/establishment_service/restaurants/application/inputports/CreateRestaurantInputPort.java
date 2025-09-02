package com.sa.establishment_service.restaurants.application.inputports;

import com.sa.establishment_service.restaurants.application.dtos.CreateRestaurantDTO;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreateRestaurantInputPort {
    public Restaurant handle(@Valid CreateRestaurantDTO createRestaurantDTO) throws NotFoundException;
}
