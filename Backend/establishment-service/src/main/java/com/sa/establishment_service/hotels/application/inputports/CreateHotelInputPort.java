package com.sa.establishment_service.hotels.application.inputports;

import com.sa.establishment_service.hotels.application.dtos.CreateHotelDTO;
import com.sa.establishment_service.hotels.domain.Hotel;

import jakarta.validation.Valid;

public interface CreateHotelInputPort {
    public Hotel handle(@Valid CreateHotelDTO createHotelDTO);
}
