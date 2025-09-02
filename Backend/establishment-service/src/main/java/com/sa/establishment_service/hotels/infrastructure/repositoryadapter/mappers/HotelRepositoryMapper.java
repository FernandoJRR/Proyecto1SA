package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;

@Mapper(componentModel = "spring")
public interface HotelRepositoryMapper {
    public List<Hotel> toDomainList(List<HotelEntity> hotelEntities);
    @Mapping(target = "restaurants", ignore = true)
    public HotelEntity toEntity(Hotel hotel);
    @Mapping(target = "restaurants", ignore = true)
    public Hotel toDomain(HotelEntity hotelEntity);
}
