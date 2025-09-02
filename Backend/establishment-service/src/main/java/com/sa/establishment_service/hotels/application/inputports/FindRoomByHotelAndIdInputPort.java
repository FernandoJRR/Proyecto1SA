package com.sa.establishment_service.hotels.application.inputports;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.NotFoundException;

public interface FindRoomByHotelAndIdInputPort {
    public Room handle(String hotelId, String roomId) throws NotFoundException;
}
