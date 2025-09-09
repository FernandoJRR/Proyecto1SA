package com.sa.client_service.orders.application.outputports;

import java.util.List;

import com.sa.client_service.orders.application.dtos.FindOrdersDTO;
import com.sa.client_service.orders.domain.Order;

public interface FindAllOrdersOutputPort {
    public List<Order> findAll(FindOrdersDTO filter);
}
