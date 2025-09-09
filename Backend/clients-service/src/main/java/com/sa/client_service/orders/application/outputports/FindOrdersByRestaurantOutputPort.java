package com.sa.client_service.orders.application.outputports;

import java.util.List;

import com.sa.client_service.orders.domain.Order;

public interface FindOrdersByRestaurantOutputPort {
    public List<Order> findByRestaurant(String restaurantId);
}
