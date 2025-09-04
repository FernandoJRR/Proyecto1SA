package com.sa.client_service.orders.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.orders.application.inputports.FindOrderByIdInputPort;
import com.sa.client_service.orders.application.outputports.FindOrderByIdOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindOrderByIdUseCase implements FindOrderByIdInputPort {

    private final FindOrderByIdOutputPort findOrderByIdOutputPort;

    @Override
    public Order handle(String orderId) throws NotFoundException {
        return findOrderByIdOutputPort.findById(orderId)
            .orElseThrow(() -> new NotFoundException("La orden buscada no fue encontrada."));
    }

}
