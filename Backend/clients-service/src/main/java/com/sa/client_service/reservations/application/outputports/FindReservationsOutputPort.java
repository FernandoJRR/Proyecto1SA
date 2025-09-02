package com.sa.client_service.reservations.application.outputports;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.sa.client_service.reservations.domain.Reservation;

public interface FindReservationsOutputPort {
    public List<Reservation> findReservations(
        UUID hotelId,
        UUID roomId,
        String clientCui,
        LocalDate startDate,
        LocalDate endDate
    );
}
