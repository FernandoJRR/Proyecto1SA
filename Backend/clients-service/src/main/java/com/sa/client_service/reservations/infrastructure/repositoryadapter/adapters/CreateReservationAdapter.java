package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.client_service.reservations.application.outputports.CreateReservationOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateReservationAdapter implements CreateReservationOutputPort {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryMapper reservationRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Reservation createReservation(Reservation reservation) {
        ReservationEntity reservationEntity = reservationRepositoryMapper.toEntity(reservation);
        ReservationEntity savedReservation = reservationRepository.save(reservationEntity);
        return reservationRepositoryMapper.toDomain(savedReservation);
    }

}
