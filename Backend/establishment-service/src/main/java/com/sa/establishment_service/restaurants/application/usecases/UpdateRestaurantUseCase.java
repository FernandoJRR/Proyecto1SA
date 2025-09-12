package com.sa.establishment_service.restaurants.application.usecases;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.dtos.UpdateRestaurantDTO;
import com.sa.establishment_service.restaurants.application.inputports.UpdateRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.UpdateRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdateRestaurantUseCase implements UpdateRestaurantInputPort {

    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private final UpdateRestaurantOutputPort updateRestaurantOutputPort;

    @Override
    @Transactional
    public Restaurant handle(String restaurantId, @Valid UpdateRestaurantDTO updateRestaurantDTO) throws NotFoundException {
        Restaurant existing = findRestaurantByIdOutputPort.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("El restaurante buscado no existe"));

        existing.setName(updateRestaurantDTO.getName());
        existing.setAddress(updateRestaurantDTO.getAddress());

        return updateRestaurantOutputPort.updateRestaurant(existing);
    }
}

