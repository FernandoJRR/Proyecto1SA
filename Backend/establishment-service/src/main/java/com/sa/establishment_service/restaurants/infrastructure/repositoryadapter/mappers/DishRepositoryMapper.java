package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;

@Mapper(componentModel = "spring")
public interface DishRepositoryMapper {
    public DishEntity toEntity(Dish dish);
    public Dish toDomain(DishEntity dishEntity);
    public List<Dish> toDomain(List<DishEntity> dishEntity);
}
