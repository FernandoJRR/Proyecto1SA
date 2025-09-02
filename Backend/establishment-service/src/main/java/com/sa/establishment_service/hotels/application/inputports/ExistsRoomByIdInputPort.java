package com.sa.establishment_service.hotels.application.inputports;

import com.sa.shared.exceptions.NotFoundException;

public interface ExistsRoomByIdInputPort {
    public void handle(String roomId) throws NotFoundException;
}
