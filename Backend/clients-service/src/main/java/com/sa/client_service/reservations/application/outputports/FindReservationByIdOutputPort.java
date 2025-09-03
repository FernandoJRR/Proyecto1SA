package com.sa.client_service.reservations.application.outputports;

import java.util.Optional;

import com.sa.client_service.reservations.domain.Reservation;

public interface FindReservationByIdOutputPort {
    public Optional<Reservation> findById(String reservationId);
}
