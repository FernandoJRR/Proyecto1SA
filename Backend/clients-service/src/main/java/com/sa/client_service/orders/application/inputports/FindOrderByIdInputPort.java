package com.sa.client_service.orders.application.inputports;

import com.sa.client_service.orders.domain.Order;
import com.sa.shared.exceptions.NotFoundException;

public interface FindOrderByIdInputPort {
    public Order handle(String orderId) throws NotFoundException;
}
