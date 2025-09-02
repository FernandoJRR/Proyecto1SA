package com.sa.establishment_service.hotels.application.inputports;

import com.sa.shared.exceptions.NotFoundException;

public interface ExistsRoomInHotelByIdInputPort {
    public void handle(String hotelId, String roomId) throws NotFoundException;
}
