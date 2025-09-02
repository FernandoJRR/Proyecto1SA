package com.sa.establishment_service.restaurants.infrastructure.restadapter.mappers;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;

import com.sa.establishment_service.restaurants.application.dtos.CreateDishDTO;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesDTO;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.CreateDishRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.DishResponse;

@Mapper(componentModel = "spring")
public interface DishRestMapper {
    public CreateDishDTO toDTO(CreateDishRequest createDishRequest);
    public DishResponse toResponse(Dish dish);
    public List<DishResponse> toResponse(List<Dish> dish);
}
