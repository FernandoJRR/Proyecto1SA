package com.sa.client_service.reservations.application.inputports;

import com.sa.client_service.reservations.application.dtos.MostPopularRoomDTO;
import com.sa.shared.exceptions.NotFoundException;

public interface GetMostPopularRoomInputPort {
    public MostPopularRoomDTO handle(String hotelId) throws NotFoundException;
}
