package com.sa.establishment_service.restaurants.application.outputports;

import com.sa.establishment_service.restaurants.domain.Restaurant;

public interface CreateRestaurantOutputPort {
    public Restaurant createRestaurant(Restaurant restaurant);
}
