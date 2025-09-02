package com.sa.client_service.orders.application.outputports;

import com.sa.client_service.orders.domain.Order;

public interface CreateOrderOutputPort {
    public Order createOrder(Order order);
}
