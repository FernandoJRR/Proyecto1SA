package com.sa.finances_service.promotions.application.outputports;

import java.util.List;
import java.util.UUID;

public interface MostPopularDishesOutputPort {
    public List<UUID> findMostPopular(String restaurantId, Integer limit);
}
