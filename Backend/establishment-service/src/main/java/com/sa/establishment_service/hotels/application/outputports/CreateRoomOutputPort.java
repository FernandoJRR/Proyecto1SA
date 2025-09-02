package com.sa.establishment_service.hotels.application.outputports;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

public interface CreateRoomOutputPort {
    public Room createRoom(String hotelId, Room room) throws NotFoundException, DuplicatedEntryException;
}
