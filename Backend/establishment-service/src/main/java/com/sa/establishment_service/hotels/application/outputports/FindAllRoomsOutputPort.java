package com.sa.establishment_service.hotels.application.outputports;

import java.util.List;

import com.sa.establishment_service.hotels.application.dtos.RoomWithHotelDTO;
import com.sa.establishment_service.hotels.domain.Room;

public interface FindAllRoomsOutputPort {
    public List<RoomWithHotelDTO> findAll();
}
