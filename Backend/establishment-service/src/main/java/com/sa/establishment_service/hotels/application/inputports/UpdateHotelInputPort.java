package com.sa.establishment_service.hotels.application.inputports;

import com.sa.establishment_service.hotels.application.dtos.UpdateHotelDTO;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface UpdateHotelInputPort {
    public Hotel handle(String hotelId, @Valid UpdateHotelDTO updateHotelDTO) throws NotFoundException;
}

