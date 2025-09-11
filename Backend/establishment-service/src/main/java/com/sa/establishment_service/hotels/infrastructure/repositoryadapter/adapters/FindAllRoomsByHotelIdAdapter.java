package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.FindAllRoomdByHotelIdOutputPort;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllRoomsByHotelIdAdapter implements FindAllRoomdByHotelIdOutputPort {

    private final RoomRepository roomRepository;
    private final RoomRepositoryMapper roomRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Room> findByHotelId(UUID hotelId) {
        return roomRepositoryMapper.toDomainList(roomRepository.findAllByHotel_Id(hotelId.toString()));
    }

}
