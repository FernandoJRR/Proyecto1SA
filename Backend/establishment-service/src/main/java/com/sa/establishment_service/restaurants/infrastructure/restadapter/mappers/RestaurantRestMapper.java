package com.sa.establishment_service.restaurants.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.establishment_service.restaurants.application.dtos.CreateRestaurantDTO;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.CreateRestaurantRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.RestaurantResponse;

@Mapper(componentModel = "spring")
public interface RestaurantRestMapper {
    public CreateRestaurantDTO toDTO(CreateRestaurantRequest createRestaurantRequest);
    public RestaurantResponse toResponse(Restaurant restaurant);
}
