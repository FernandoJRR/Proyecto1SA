package com.sa.client_service.orders.application.outputports;

import java.util.Optional;

import com.sa.client_service.orders.domain.Order;

public interface FindOrderByIdOutputPort {
    public Optional<Order> findById(String orderId);
}
