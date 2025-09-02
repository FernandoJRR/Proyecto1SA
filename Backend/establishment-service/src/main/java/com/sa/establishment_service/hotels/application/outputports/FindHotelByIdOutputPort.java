package com.sa.establishment_service.hotels.application.outputports;

import java.util.Optional;
import java.util.UUID;

import com.sa.establishment_service.hotels.domain.Hotel;

public interface FindHotelByIdOutputPort {
    public Optional<Hotel> findHotelById(UUID id);
}
