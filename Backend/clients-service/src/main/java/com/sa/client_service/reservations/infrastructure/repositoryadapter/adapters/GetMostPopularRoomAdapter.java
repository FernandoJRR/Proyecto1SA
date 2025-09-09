package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sa.client_service.reservations.application.outputports.GetMostPopularRoomOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetMostPopularRoomAdapter implements GetMostPopularRoomOutputPort {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryMapper reservationRepositoryMapper;

    @Override
    public Optional<Reservation> getMostPopular(String hotelId) {
        if (hotelId != null) {
            List<ReservationEntity> obtainedReservations = reservationRepository.findTopRoomsByHotel(UUID.fromString(hotelId), (Pageable) PageRequest.of(0,1));
            if (obtainedReservations.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(reservationRepositoryMapper.toDomain(obtainedReservations.get(0)));
            }
        } else {
            List<ReservationEntity> obtainedReservations = reservationRepository.findTopRoomsGlobally((Pageable) PageRequest.of(0,1));
            if (obtainedReservations.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(reservationRepositoryMapper.toDomain(obtainedReservations.get(0)));
            }
        }
    }

}
