package com.sa.client_service.reservations.application.outputports;

import java.util.List;

import com.sa.client_service.reservations.application.dtos.RoomDTO;

public interface FindAllRoomsOutputPort {
    public List<RoomDTO> findAll();
}
