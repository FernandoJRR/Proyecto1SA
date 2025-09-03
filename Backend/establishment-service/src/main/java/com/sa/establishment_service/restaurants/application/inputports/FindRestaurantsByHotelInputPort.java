package com.sa.establishment_service.restaurants.application.inputports;

import java.util.List;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

public interface FindRestaurantsByHotelInputPort {
    List<Restaurant> handle(String hotelId) throws NotFoundException;
}
