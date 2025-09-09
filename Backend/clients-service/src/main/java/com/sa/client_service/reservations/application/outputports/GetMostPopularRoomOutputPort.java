package com.sa.client_service.reservations.application.outputports;

import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.reservations.domain.Reservation;

public interface GetMostPopularRoomOutputPort {
    public Optional<Reservation> getMostPopular(String hotelId);
}
