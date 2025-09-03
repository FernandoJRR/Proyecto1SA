package com.sa.establishment_service.restaurants.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.establishment_service.restaurants.application.dtos.CreateRestaurantDTO;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.CreateRestaurantRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.RestaurantResponse;

@Mapper(componentModel = "spring")
public interface RestaurantRestMapper {
    public CreateRestaurantDTO toDTO(CreateRestaurantRequest createRestaurantRequest);
    @Mapping(target = "hotelId", source = "hotel.id")
    public RestaurantResponse toResponse(Restaurant restaurant);
    public List<RestaurantResponse> toResponse(List<Restaurant> restaurant);
}
