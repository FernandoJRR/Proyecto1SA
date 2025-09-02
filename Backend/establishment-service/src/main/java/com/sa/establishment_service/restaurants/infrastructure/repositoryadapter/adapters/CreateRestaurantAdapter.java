package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import com.sa.establishment_service.restaurants.application.outputports.CreateRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateRestaurantAdapter implements CreateRestaurantOutputPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryMapper restaurantRepositoryMapper;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantRepositoryMapper.toEntity(restaurant);

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurantEntity);

        return restaurantRepositoryMapper.toDomain(savedRestaurant);
    }
}
