package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.function.Supplier;

import com.sa.establishment_service.restaurants.application.outputports.CreateDishOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateDishAdapter implements CreateDishOutputPort {

    private final DishRepositoryMapper dishRepositoryMapper;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Dish createDish(String restaurantId, Dish dish) throws NotFoundException {
        RestaurantEntity foundRestaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow( () -> new NotFoundException("Restaurante no encontrado"));

        DishEntity dishEntity = dishRepositoryMapper.toEntity(dish);
        dishEntity.setRestaurant(foundRestaurant);

        DishEntity savedDish = dishRepository.save(dishEntity);
        return dishRepositoryMapper.toDomain(savedDish);
    }

}
