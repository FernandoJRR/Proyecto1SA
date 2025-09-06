package com.sa.client_service.reservations.application.inputports;

import java.util.List;
import java.util.UUID;

public interface MostPopularRoomsInputPort {
    public List<UUID> handle(UUID hotelId);
}
