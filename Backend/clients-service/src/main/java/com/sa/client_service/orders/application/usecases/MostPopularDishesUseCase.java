package com.sa.client_service.orders.application.usecases;

import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.orders.application.inputports.MostPopularDishesInputPort;
import com.sa.client_service.orders.application.outputports.MostPopularDishesOutputPort;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class MostPopularDishesUseCase implements MostPopularDishesInputPort {

    private final MostPopularDishesOutputPort mostPopularDishesOutputPort;

    @Override
    public List<UUID> handle(UUID restaurantId) {
        return mostPopularDishesOutputPort.mostPopularDishes(restaurantId);
    }

}
