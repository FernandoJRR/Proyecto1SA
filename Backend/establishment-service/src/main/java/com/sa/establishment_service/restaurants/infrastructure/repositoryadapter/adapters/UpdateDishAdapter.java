package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.restaurants.application.outputports.UpdateDishOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UpdateDishAdapter implements UpdateDishOutputPort {

    private final DishRepository dishRepository;
    private final DishRepositoryMapper dishRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Dish updateDish(Dish dish) {
        DishEntity existing = dishRepository.findById(dish.getId().toString())
            .orElseThrow(() -> new IllegalStateException("Dish to update not found"));

        existing.setName(dish.getName());
        existing.setPrice(dish.getPrice());

        DishEntity saved = dishRepository.save(existing);
        return dishRepositoryMapper.toDomain(saved);
    }
}

