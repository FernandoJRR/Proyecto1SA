package com.sa.establishment_service.hotels.application.outputports;

import java.util.Optional;

import com.sa.establishment_service.hotels.domain.Room;

public interface FindRoomByHotelAndIdOutputPort {
    public Optional<Room> findByHotelAndId(String hotelId, String roomId);
}
