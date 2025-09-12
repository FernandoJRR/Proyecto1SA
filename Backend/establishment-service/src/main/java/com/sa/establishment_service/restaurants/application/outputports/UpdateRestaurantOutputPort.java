package com.sa.establishment_service.restaurants.application.outputports;

import com.sa.establishment_service.restaurants.domain.Restaurant;

public interface UpdateRestaurantOutputPort {
    public Restaurant updateRestaurant(Restaurant restaurant);
}

