package com.sa.client_service.orders.application.outputports;

import java.util.Optional;

import com.sa.client_service.orders.application.dtos.RestaurantDTO;

public interface FindRestaurantByIdOutputPort {
    public Optional<RestaurantDTO> findById(String restaurantId);
}
