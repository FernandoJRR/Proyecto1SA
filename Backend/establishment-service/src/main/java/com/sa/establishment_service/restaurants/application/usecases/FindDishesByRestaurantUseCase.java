package com.sa.establishment_service.restaurants.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.inputports.FindDishesByRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindDishesByRestaurantOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindDishesByRestaurantUseCase implements FindDishesByRestaurantInputPort {

    private final FindDishesByRestaurantOutputPort findDishesByRestaurantOutputPort;
    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;

    @Override
    public List<Dish> handle(String id) throws NotFoundException {
        findRestaurantByIdOutputPort.findById(id)
            .orElseThrow(() -> new NotFoundException("El restaurante ingresado no existe."));

        return findDishesByRestaurantOutputPort.findByRestaurant(id);
    }

}
