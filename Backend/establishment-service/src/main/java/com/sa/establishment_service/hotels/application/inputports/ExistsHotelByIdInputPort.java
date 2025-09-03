package com.sa.establishment_service.hotels.application.inputports;

import com.sa.shared.exceptions.NotFoundException;

public interface ExistsHotelByIdInputPort {
    public void handle(String id) throws NotFoundException;
}
