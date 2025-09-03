package com.sa.establishment_service.restaurants.application.outputports;

import java.util.List;

import com.sa.establishment_service.restaurants.domain.Restaurant;

public interface FindRestaurantByHotelOutputPort {
    public List<Restaurant> findByHotel(String hotelId);
}
