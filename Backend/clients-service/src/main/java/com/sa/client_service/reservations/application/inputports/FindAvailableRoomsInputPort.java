package com.sa.client_service.reservations.application.inputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.client_service.reservations.application.dtos.RoomDTO;

public interface FindAvailableRoomsInputPort {
    public List<RoomDTO> handle(String hotelId, LocalDate startDate, LocalDate endDate);
}
