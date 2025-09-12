package com.sa.establishment_service.restaurants.application.usecases;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.dtos.UpdateDishDTO;
import com.sa.establishment_service.restaurants.application.inputports.UpdateDishInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindDishByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.UpdateDishOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdateDishUseCase implements UpdateDishInputPort {

    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private final FindDishByIdOutputPort findDishByIdOutputPort;
    private final UpdateDishOutputPort updateDishOutputPort;

    @Override
    @Transactional
    public Dish handle(String restaurantId, String dishId, @Valid UpdateDishDTO updateDishDTO) throws NotFoundException {
        findRestaurantByIdOutputPort.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("El restaurante buscado no existe"));

        Dish existing = findDishByIdOutputPort.findById(dishId)
            .orElseThrow(() -> new NotFoundException("El platillo buscado no existe"));

        existing.setName(updateDishDTO.getName());
        existing.setPrice(updateDishDTO.getPrice());

        return updateDishOutputPort.updateDish(existing);
    }
}

