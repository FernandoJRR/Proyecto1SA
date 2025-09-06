package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;

import com.sa.client_service.reservations.application.outputports.MostPopularRoomsOutputPort;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MostPopularRoomsAdapter implements MostPopularRoomsOutputPort {

    private final ReservationRepository reservationRepository;

    @Override
    public List<UUID> mostPopularRooms(UUID hotelId) {
        return reservationRepository.findTopRoomsByHotel(hotelId);
    }

}
