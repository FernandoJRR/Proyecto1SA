package com.sa.establishment_service.restaurants.application.usecases;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.dtos.CreateDishDTO;
import com.sa.establishment_service.restaurants.application.inputports.CreateDishInputPort;
import com.sa.establishment_service.restaurants.application.outputports.CreateDishOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreateDishUseCase implements CreateDishInputPort {

    private final CreateDishOutputPort createDishOutputPort;
    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;


    @Override
    public Dish handle(String restaurantId, @Valid CreateDishDTO createDishDTO) throws NotFoundException {
        findRestaurantByIdOutputPort.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurante no encontrado"));

        Dish createdDish = Dish.create(createDishDTO.getName(), createDishDTO.getPrice());
        return createDishOutputPort.createDish(restaurantId.toString(), createdDish);
    }
}
