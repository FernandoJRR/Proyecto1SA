package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindRestaurantByIdAdapter implements FindRestaurantByIdOutputPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryMapper restaurantRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurant> findById(String id) {
        return restaurantRepository.findById(id)
            .map(restaurantRepositoryMapper::toDomain);
    }

}
