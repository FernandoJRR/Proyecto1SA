package com.sa.client_service.reservations.application.outputports;

import java.util.List;
import java.util.UUID;

public interface MostPopularRoomsOutputPort {
    public List<UUID> mostPopularRooms(UUID hotelId);
}
