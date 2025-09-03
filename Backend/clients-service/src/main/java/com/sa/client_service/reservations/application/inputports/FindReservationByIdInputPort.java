package com.sa.client_service.reservations.application.inputports;

import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.NotFoundException;

public interface FindReservationByIdInputPort {
    public Reservation handle(String reservationId) throws NotFoundException;
}
