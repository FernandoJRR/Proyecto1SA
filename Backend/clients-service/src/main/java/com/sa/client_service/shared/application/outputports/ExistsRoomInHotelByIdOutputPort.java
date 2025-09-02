package com.sa.client_service.shared.application.outputports;

public interface ExistsRoomInHotelByIdOutputPort {
    public boolean existsById(String hotelId, String roomId);
}
