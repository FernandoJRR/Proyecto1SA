package com.sa.establishment_service.restaurants.application.outputports;

import java.util.List;

import com.sa.establishment_service.restaurants.domain.Restaurant;

public interface FindAllRestaurantsOutputPort {
    public List<Restaurant> findAll();
}
