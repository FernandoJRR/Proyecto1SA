package com.sa.client_service.orders.application.inputports;

import java.util.List;
import java.util.UUID;

public interface MostPopularDishesInputPort {
    public List<UUID> handle(UUID restaurantId);
}
