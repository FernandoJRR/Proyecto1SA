package com.sa.establishment_service.restaurants.application.inputports;

import java.util.List;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

public interface FindDishesByRestaurantInputPort {
    public List<Dish> handle(String id) throws NotFoundException;
}
