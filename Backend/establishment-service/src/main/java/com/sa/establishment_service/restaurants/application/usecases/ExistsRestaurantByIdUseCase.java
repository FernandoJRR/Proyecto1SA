package com.sa.establishment_service.restaurants.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.inputports.ExistsRestaurantByIdInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ExistsRestaurantByIdUseCase implements ExistsRestaurantByIdInputPort {

    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;

    @Override
    public void handle(String restaurantId) throws NotFoundException {
        findRestaurantByIdOutputPort.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("El restaurante buscado no existe"));
    }

}
