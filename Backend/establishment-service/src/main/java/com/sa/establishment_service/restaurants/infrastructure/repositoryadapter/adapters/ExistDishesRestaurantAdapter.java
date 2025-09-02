package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sa.establishment_service.restaurants.application.outputports.ExistDishesRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ExistDishesRestaurantAdapter implements ExistDishesRestaurantOutputPort {

    private final DishRepository dishRepository;
    private final DishRepositoryMapper dishRepositoryMapper;

    @Override
    public List<Dish> findDishesByRestaurantAndIds(String restaurantId, List<String> presentIds) {
        return dishRepositoryMapper.toDomain(dishRepository.findExistant(restaurantId, presentIds));
    }

}
