package com.sa.client_service.reservations.application.outputports;

import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;

public interface ReservationHydrationOutputPort {
    public HotelDTO getHotel(String id);
    public RoomDTO getRoom(String hotelId, String roomId);
}
