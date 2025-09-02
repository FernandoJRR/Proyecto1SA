package com.sa.client_service.reservations.application.inputports;

import com.sa.client_service.reservations.application.dtos.CreateReservationDTO;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreateReservationInputPort {
    public Reservation handle(@Valid CreateReservationDTO createReservationDTO) throws NotFoundException, InvalidParameterException;
}
