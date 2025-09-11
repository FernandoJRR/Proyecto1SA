package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.ExistsRoomByIdOutputPort;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ExistsRoomByIdAdapter implements ExistsRoomByIdOutputPort {

    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String roomId) {
        return roomRepository.existsById(roomId);
    }

}
