package com.sa.establishment_service.restaurants.application.outputports;

import java.util.Optional;

import com.sa.establishment_service.restaurants.domain.Restaurant;

public interface FindRestaurantByIdOutputPort {
    public Optional<Restaurant> findById(String id);
}
