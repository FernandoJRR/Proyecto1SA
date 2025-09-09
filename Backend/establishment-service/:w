package com.sa.establishment_service.restaurants.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.inputports.FindDishByIdInputPort;
import com.sa.establishment_service.restaurants.application.outputports.ExistDishesRestaurantOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindDishByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindDishByIdUseCase implements FindDishByIdInputPort {

    private final FindDishByIdOutputPort findDishByIdOutputPort;
    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;

    @Override
    public Dish handle(String restaurantId, String dishId) throws NotFoundException {
        findRestaurantByIdOutputPort.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("El restaurante buscado no existe"));

        return findDishByIdOutputPort.findById(dishId)
            .orElseThrow(() -> new NotFoundException("El platillo buscado no existe"));
    }

}
