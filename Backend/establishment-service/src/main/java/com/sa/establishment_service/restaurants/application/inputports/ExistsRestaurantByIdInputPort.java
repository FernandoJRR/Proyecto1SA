package com.sa.establishment_service.restaurants.application.inputports;

import com.sa.shared.exceptions.NotFoundException;

public interface ExistsRestaurantByIdInputPort {
    public void handle(String restaurantId) throws NotFoundException;
}
