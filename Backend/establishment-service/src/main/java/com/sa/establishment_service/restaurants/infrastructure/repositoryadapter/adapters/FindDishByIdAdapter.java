package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.restaurants.application.outputports.FindDishByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindDishByIdAdapter implements FindDishByIdOutputPort{

    private final DishRepository dishRepository;
    private final DishRepositoryMapper dishRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Dish> findById(String id) {
        return dishRepository.findById(id).map(dishRepositoryMapper::toDomain);
    }

}
