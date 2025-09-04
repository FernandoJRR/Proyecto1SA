package com.sa.finances_service.payments.application.outputports;

import com.sa.finances_service.payments.application.dtos.HotelDTO;
import com.sa.finances_service.payments.application.dtos.RestaurantDTO;

public interface PaymentHydrationOutputPort {
    public HotelDTO getHotel(String id);
    public RestaurantDTO getRestaurant(String id);
}
