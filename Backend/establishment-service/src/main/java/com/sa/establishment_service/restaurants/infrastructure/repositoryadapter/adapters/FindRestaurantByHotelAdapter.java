package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import java.util.List;

import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByHotelOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindRestaurantByHotelAdapter implements FindRestaurantByHotelOutputPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryMapper restaurantRepositoryMapper;

    @Override
    public List<Restaurant> findByHotel(String hotelId) {
        List<RestaurantEntity> result = restaurantRepository.findAllByHotel_Id(hotelId);
        return restaurantRepositoryMapper.toDomain(result);
    }

}
