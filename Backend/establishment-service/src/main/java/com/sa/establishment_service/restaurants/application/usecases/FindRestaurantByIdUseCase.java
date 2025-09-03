package com.sa.establishment_service.restaurants.application.usecases;

import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.inputports.FindRestaurantByIdInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindRestaurantByIdUseCase implements FindRestaurantByIdInputPort {

    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;

    @Override
    public Restaurant handle(String id) throws NotFoundException {
        return findRestaurantByIdOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("El restaurante buscado no existe"));
    }

}
