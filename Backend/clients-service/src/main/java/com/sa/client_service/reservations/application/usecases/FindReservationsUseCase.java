package com.sa.client_service.reservations.application.usecases;

import java.time.LocalDate;
import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.reservations.application.dtos.FindReservationsDTO;
import com.sa.client_service.reservations.application.inputports.FindReservationsInputPort;
import com.sa.client_service.reservations.application.outputports.FindReservationsOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.InvalidParameterException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindReservationsUseCase implements FindReservationsInputPort {

    private final FindReservationsOutputPort findReservationsOutputPort;

    @Override
    public List<Reservation> handle(FindReservationsDTO findReservationsDTO) throws InvalidParameterException {
        LocalDate start = findReservationsDTO.getStartDate();
        LocalDate end = findReservationsDTO.getEndDate();

        if (start != null && end != null && start.isAfter(end)) {
            throw new InvalidParameterException("La fecha de inicio debe ser antes o igual que la fecha de fin");
        }

        return findReservationsOutputPort.findReservations(findReservationsDTO.getHotelId(), findReservationsDTO.getRoomId(),
                findReservationsDTO.getClientCui(), findReservationsDTO.getStartDate(),
                findReservationsDTO.getEndDate());
    }

}
