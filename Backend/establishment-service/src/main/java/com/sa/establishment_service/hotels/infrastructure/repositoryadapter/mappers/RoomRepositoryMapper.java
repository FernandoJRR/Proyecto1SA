package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;

@Mapper(componentModel = "spring")
public interface RoomRepositoryMapper {
    public RoomEntity toEntity(Room room);
    public Room toDomain(RoomEntity roomEntity);
    public List<Room> toDomainList(List<RoomEntity> roomEntities);
}
