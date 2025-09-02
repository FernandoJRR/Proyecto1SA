package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import com.sa.establishment_service.hotels.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ExistsRoomInHotelByIdAdapter implements ExistsRoomInHotelByIdOutputPort {

    private final RoomRepository roomRepository;

    @Override
    public boolean existsRoomInHotel(String hotelId, String roomId) {
        return roomRepository.existsById(roomId) &&
        roomRepository.findById(roomId)
                      .map(room -> room.getHotel().getId().equals(hotelId))
                      .orElse(false);
    }

}
