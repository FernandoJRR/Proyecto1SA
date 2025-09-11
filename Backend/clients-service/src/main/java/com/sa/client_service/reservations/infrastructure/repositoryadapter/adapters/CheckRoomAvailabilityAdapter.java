package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.sa.client_service.reservations.application.outputports.CheckRoomAvailabilityOutputPort;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CheckRoomAvailabilityAdapter implements CheckRoomAvailabilityOutputPort {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(UUID hotelId, UUID roomId, LocalDate startDate, LocalDate endDate) {
        boolean anyOverlap = reservationRepository.existsOverlappingReservation(
                hotelId, roomId, startDate, endDate);
        return !anyOverlap;
    }

}
