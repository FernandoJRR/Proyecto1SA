package com.sa.establishment_service.restaurants.application.inputports;

import java.util.List;

import com.sa.establishment_service.restaurants.domain.Restaurant;

public interface FindAllRestaurantsInputPort {
    public List<Restaurant> handle();
}
