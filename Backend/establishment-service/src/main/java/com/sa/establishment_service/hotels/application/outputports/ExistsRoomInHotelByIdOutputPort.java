package com.sa.establishment_service.hotels.application.outputports;

public interface ExistsRoomInHotelByIdOutputPort {
    public boolean existsRoomInHotel(String hotelId, String roomId);
}
