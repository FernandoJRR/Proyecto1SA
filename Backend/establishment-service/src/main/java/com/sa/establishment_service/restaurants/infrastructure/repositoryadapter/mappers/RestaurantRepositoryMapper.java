package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;

@Mapper(componentModel = "spring", uses = {DishRepositoryMapper.class, HotelRepositoryMapper.class})
public interface RestaurantRepositoryMapper {
    public RestaurantEntity toEntity(Restaurant restaurant);

    public Restaurant toDomain(RestaurantEntity restaurantEntity);
    public List<Restaurant> toDomain(List<RestaurantEntity> restaurantEntity);
}
