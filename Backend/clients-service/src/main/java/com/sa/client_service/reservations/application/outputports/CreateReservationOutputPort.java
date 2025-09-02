package com.sa.client_service.reservations.application.outputports;

import com.sa.client_service.reservations.domain.Reservation;

public interface CreateReservationOutputPort {
    public Reservation createReservation(Reservation reservation);
}
