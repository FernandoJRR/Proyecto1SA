package com.sa.client_service.orders.application.inputports;

import com.sa.client_service.orders.application.dtos.MostPopularRestaurantDTO;
import com.sa.shared.exceptions.NotFoundException;

public interface MostPopularRestaurantInputPort {
    public MostPopularRestaurantDTO handle() throws NotFoundException;
}
