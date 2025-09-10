package com.sa.establishment_service.hotels.application.inputports;

import java.util.List;

import com.sa.establishment_service.hotels.application.dtos.RoomWithHotelDTO;
import com.sa.establishment_service.hotels.domain.Room;

public interface FindAllRoomsInputPort {
    public List<RoomWithHotelDTO> handle();
}
