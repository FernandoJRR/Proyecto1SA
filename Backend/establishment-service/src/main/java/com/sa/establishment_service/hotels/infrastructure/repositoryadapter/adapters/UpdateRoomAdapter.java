package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.UpdateRoomOutputPort;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UpdateRoomAdapter implements UpdateRoomOutputPort {

    private final RoomRepository roomRepository;
    private final RoomRepositoryMapper roomRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Room updateRoom(Room room) {
        RoomEntity existing = roomRepository.findById(room.getId().toString())
            .orElseThrow(() -> new IllegalStateException("Room to update not found"));

        existing.setNumber(room.getNumber());
        existing.setPricePerNight(room.getPricePerNight());
        existing.setCapacity(room.getCapacity());

        RoomEntity saved = roomRepository.save(existing);
        return roomRepositoryMapper.toDomain(saved);
    }
}
