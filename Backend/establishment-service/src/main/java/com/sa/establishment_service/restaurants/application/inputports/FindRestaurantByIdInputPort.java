package com.sa.establishment_service.restaurants.application.inputports;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

public interface FindRestaurantByIdInputPort {
    public Restaurant handle(String id) throws NotFoundException;
}
