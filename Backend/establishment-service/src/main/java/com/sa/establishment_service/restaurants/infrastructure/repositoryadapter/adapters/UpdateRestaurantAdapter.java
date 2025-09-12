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
        RestaurantEntity existing = restaurantRepository.findById(restaurant.getId().toString())
            .orElseThrow(() -> new IllegalStateException("Restaurant to update not found"));

        // Only update scalar fields to avoid altering dish associations
        existing.setName(restaurant.getName());
        existing.setAddress(restaurant.getAddress());

        RestaurantEntity saved = restaurantRepository.save(existing);
        return restaurantRepositoryMapper.toDomain(saved);
    }
}
