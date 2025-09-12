package com.sa.establishment_service.restaurants.application.outputports;

import com.sa.establishment_service.restaurants.domain.Dish;

public interface UpdateDishOutputPort {
    public Dish updateDish(Dish dish);
}

