package com.sa.establishment_service.hotels.application.inputports;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.shared.exceptions.NotFoundException;

public interface FindHotelByIdInputPort {
    public Hotel handle(String id) throws NotFoundException;
}
