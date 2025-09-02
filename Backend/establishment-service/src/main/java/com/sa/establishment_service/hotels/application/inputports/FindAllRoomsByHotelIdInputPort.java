package com.sa.establishment_service.hotels.application.inputports;

import java.util.List;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.NotFoundException;

public interface FindAllRoomsByHotelIdInputPort {
    public List<Room> handle(String hotelId) throws NotFoundException;
}
