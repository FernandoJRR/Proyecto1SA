package com.sa.establishment_service.restaurants.application.inputports;

import com.sa.establishment_service.restaurants.application.dtos.UpdateRestaurantDTO;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface UpdateRestaurantInputPort {
    public Restaurant handle(String restaurantId, @Valid UpdateRestaurantDTO updateRestaurantDTO) throws NotFoundException;
}

