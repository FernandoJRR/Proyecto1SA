package com.sa.client_service.reservations.application.outputports;

import java.util.Optional;

import com.sa.client_service.reservations.application.dtos.RoomDTO;

public interface FindRoomByHotelAndIdOutputPort {
    public Optional<RoomDTO> findByHotelAndId(String hotelId, String roomId);
}
