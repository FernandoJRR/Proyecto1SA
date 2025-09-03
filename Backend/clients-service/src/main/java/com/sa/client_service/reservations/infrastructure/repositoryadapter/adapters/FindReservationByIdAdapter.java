package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import com.sa.client_service.reservations.application.outputports.FindReservationByIdOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindReservationByIdAdapter implements FindReservationByIdOutputPort {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryMapper reservationRepositoryMapper;

    @Override
    public Optional<Reservation> findById(String reservationId) {
        return reservationRepository.findById(reservationId)
            .map(reservationRepositoryMapper::toDomain);
    }
}
