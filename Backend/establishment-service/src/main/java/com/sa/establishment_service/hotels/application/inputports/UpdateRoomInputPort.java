package com.sa.establishment_service.hotels.application.inputports;

import com.sa.establishment_service.hotels.application.dtos.UpdateRoomDTO;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface UpdateRoomInputPort {
    public Room handle(String hotelId, String roomId, @Valid UpdateRoomDTO updateRoomDTO) throws NotFoundException, DuplicatedEntryException;
}
