package com.sa.finances_service.promotions.application.outputports;

import java.util.List;
import java.util.UUID;

public interface MostPopularRoomsOutputPort {
    public List<UUID> findMostPopular(String hotelId, Integer limit);
}
