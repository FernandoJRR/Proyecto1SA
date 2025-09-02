package com.sa.client_service.orders.application.inputports;

import com.sa.client_service.orders.application.dtos.CreateOrderDTO;
import com.sa.client_service.orders.domain.Order;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

public interface CreateOrderInputPort {
    public Order handle(CreateOrderDTO createOrderDTO) throws NotFoundException, InvalidParameterException;
}
