package com.sa.client_service.orders.application.outputports;

import com.sa.client_service.orders.application.dtos.RestaurantDTO;

public interface OrderHydrationOutputPort {
    public RestaurantDTO getRestaurant(String restaurantId);
}
