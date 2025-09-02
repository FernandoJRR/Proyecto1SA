package com.sa.establishment_service.hotels.application.outputports;

import com.sa.establishment_service.hotels.domain.Hotel;

public interface CreateHotelOutputPort {
    public Hotel createHotel(Hotel hotel);
}
