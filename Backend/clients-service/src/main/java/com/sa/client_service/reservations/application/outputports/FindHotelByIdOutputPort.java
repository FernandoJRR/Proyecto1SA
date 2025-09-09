package com.sa.client_service.reservations.application.outputports;

import java.util.Optional;

import com.sa.client_service.reservations.application.dtos.HotelDTO;

public interface FindHotelByIdOutputPort {
    public Optional<HotelDTO> findById(String hotelId);
}
