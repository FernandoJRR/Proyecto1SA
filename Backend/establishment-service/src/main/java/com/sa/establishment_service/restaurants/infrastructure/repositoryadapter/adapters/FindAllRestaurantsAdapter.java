package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.restaurants.application.outputports.FindAllRestaurantsOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllRestaurantsAdapter implements FindAllRestaurantsOutputPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryMapper restaurantRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        return restaurantRepositoryMapper.toDomain(restaurantEntities);
    }

}
