package com.sa.client_service.orders.application.inputports;

import java.util.List;

import com.sa.client_service.orders.domain.Order;

public interface FindAllOrdersInputPort {
    public List<Order> handle();
}
