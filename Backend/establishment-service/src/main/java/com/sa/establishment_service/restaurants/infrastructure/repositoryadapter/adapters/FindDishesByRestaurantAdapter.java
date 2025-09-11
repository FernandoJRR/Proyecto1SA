package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.restaurants.application.outputports.FindDishesByRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindDishesByRestaurantAdapter implements FindDishesByRestaurantOutputPort {

    private final DishRepository dishRepository;
    private final DishRepositoryMapper dishRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Dish> findByRestaurant(String restaurantId) {
        List<DishEntity> result = dishRepository.findAllByRestaurant_Id(restaurantId);
        return dishRepositoryMapper.toDomain(result);
    }

}
