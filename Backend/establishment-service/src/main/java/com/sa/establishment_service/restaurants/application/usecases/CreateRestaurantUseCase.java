package com.sa.establishment_service.restaurants.application.usecases;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.restaurants.application.dtos.CreateRestaurantDTO;
import com.sa.establishment_service.restaurants.application.inputports.CreateRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.outputports.CreateRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreateRestaurantUseCase implements CreateRestaurantInputPort {

    private final FindHotelByIdOutputPort findHotelByIdOutputPort;
    private final CreateRestaurantOutputPort createRestaurantOutputPort;

    @Override
    public Restaurant handle(@Valid CreateRestaurantDTO createRestaurantDTO) throws NotFoundException {
        Hotel foundHotel = null;

        if (createRestaurantDTO.getHotelId() != null) {
            foundHotel = findHotelByIdOutputPort.findHotelById(UUID.fromString(createRestaurantDTO.getHotelId()))
                .orElseThrow(() -> new NotFoundException("Hotel no encontrado"));
        }

        Restaurant createdRestaurant = Restaurant.create(
                createRestaurantDTO.getName(),
                createRestaurantDTO.getAddress(),
                foundHotel
            );

        return createRestaurantOutputPort.createRestaurant(createdRestaurant);
    }

}
