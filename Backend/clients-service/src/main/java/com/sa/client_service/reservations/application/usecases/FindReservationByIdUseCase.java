package com.sa.client_service.reservations.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.reservations.application.inputports.FindReservationByIdInputPort;
import com.sa.client_service.reservations.application.outputports.FindReservationByIdOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindReservationByIdUseCase implements FindReservationByIdInputPort {

    private final FindReservationByIdOutputPort findReservationByIdOutputPort;

    @Override
    public Reservation handle(String reservationId) throws NotFoundException {
        return findReservationByIdOutputPort.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("La reservacion buscada no existe"));
    }

}
