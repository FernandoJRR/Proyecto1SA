package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import com.sa.establishment_service.hotels.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindRoomByHotelAndIdAdapter implements FindRoomByHotelAndIdOutputPort {

    private final RoomRepository roomRepository;
    private final RoomRepositoryMapper roomRepositoryMapper;

    @Override
    public Optional<Room> findByHotelAndId(String hotelId, String roomId) {
        return roomRepository.findOneByHotel_IdAndId(hotelId, roomId)
            .map(roomRepositoryMapper::toDomain);
    }

}
