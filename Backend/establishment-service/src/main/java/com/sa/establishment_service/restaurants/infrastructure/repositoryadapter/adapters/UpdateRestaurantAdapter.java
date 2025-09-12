package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.restaurants.application.outputports.UpdateRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UpdateRestaurantAdapter implements UpdateRestaurantOutputPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryMapper restaurantRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Restaurant updateRestaurant(Restaurant restaurant) {
        RestaurantEntity entity = restaurantRepositoryMapper.toEntity(restaurant);
        RestaurantEntity saved = restaurantRepository.save(entity);
        return restaurantRepositoryMapper.toDomain(saved);
    }
}

