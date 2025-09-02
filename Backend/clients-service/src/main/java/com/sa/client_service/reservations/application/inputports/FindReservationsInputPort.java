package com.sa.client_service.reservations.application.inputports;

import java.util.List;

import com.sa.client_service.reservations.application.dtos.FindReservationsDTO;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.InvalidParameterException;

public interface FindReservationsInputPort {
    public List<Reservation> handle(FindReservationsDTO findReservationsDTO) throws InvalidParameterException;
}
