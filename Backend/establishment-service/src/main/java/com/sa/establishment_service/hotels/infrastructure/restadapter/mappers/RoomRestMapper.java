package com.sa.establishment_service.hotels.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.establishment_service.hotels.application.dtos.CreateRoomDTO;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.CreateRoomRequest;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.RoomResponse;

@Mapper(componentModel = "spring")
public interface RoomRestMapper {
    public CreateRoomDTO toDTO(CreateRoomRequest createRoomRequest);
    public RoomResponse toResponse(Room room);
    public List<RoomResponse> toResponse(List<Room> rooms);
}
