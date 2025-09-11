package com.sa.establishment_service.restaurants.application.inputports;

import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.restaurants.application.dtos.ExistDishesDTO;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesResultDTO;

import jakarta.validation.Valid;

public interface ExistDishesRestaurantInputPort {
    public ExistDishesResultDTO handle(@Valid ExistDishesDTO existDishesDTO);
}
