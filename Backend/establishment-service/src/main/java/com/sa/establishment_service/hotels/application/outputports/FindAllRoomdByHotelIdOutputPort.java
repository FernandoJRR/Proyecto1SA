package com.sa.establishment_service.hotels.application.outputports;

import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.hotels.domain.Room;

public interface FindAllRoomdByHotelIdOutputPort {
    public List<Room> findByHotelId(UUID hotelId);
}
