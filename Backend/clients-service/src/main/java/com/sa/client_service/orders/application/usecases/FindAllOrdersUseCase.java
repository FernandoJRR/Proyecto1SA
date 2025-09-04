package com.sa.client_service.orders.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.orders.application.inputports.FindAllOrdersInputPort;
import com.sa.client_service.orders.application.outputports.FindAllOrdersOutputPort;
import com.sa.client_service.orders.domain.Order;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllOrdersUseCase implements FindAllOrdersInputPort {

    private final FindAllOrdersOutputPort findAllOrdersOutputPort;

    @Override
    public List<Order> handle() {
        return findAllOrdersOutputPort.findAll();
    }

}
