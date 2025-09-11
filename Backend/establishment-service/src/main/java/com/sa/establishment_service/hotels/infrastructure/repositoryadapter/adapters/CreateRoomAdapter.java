package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.CreateRoomOutputPort;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateRoomAdapter implements CreateRoomOutputPort {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    private final RoomRepositoryMapper roomRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Room createRoom(String hotelId, Room room) throws NotFoundException, DuplicatedEntryException {
        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Hotel no encontrado"));

        if (roomRepository.existsByHotel_IdAndNumber(hotelId, room.getNumber())) {
            throw new DuplicatedEntryException("Una habitacion con el mismo numero ya existe en el hotel");
        }

        RoomEntity roomEntity = roomRepositoryMapper.toEntity(room);
        roomEntity.setHotel(hotel);

        RoomEntity saved = roomRepository.save(roomEntity);

        hotel.getRooms().add(saved);

        return roomRepositoryMapper.toDomain(saved);

    }

}
