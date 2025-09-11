package com.sa.establishment_service.restaurants.application.usecases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesDTO;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesResultDTO;
import com.sa.establishment_service.restaurants.application.inputports.ExistDishesRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.outputports.ExistDishesRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class ExistDishesRestaurantUseCase implements ExistDishesRestaurantInputPort {

    private final ExistDishesRestaurantOutputPort existDishesRestaurantOutputPort;

    @Override
    public ExistDishesResultDTO handle(@Valid ExistDishesDTO existDishesDTO) {
        List<Dish> existing = existDishesRestaurantOutputPort
                .findDishesByRestaurantAndIds(existDishesDTO.getRestaurantId().toString(),
                        existDishesDTO.getDishIds().stream().map(UUID::toString).toList());

        Set<UUID> existingIds = existing.stream()
                .map(Dish::getId)
                .collect(Collectors.toSet());

        List<UUID> missing = existDishesDTO.getDishIds().stream()
                .filter(id -> !existingIds.contains(id))
                .toList();

        return new ExistDishesResultDTO(missing.isEmpty(), missing, existing);
    }

}
