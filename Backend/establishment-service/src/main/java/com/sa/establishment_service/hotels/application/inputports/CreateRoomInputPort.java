package com.sa.establishment_service.hotels.application.inputports;

import com.sa.establishment_service.hotels.application.dtos.CreateRoomDTO;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreateRoomInputPort {
    public Room handle(@Valid CreateRoomDTO createRoomDTO) throws NotFoundException, DuplicatedEntryException;
}
