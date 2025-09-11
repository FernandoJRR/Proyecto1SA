package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.dtos.RoomWithHotelDTO;
import com.sa.establishment_service.hotels.application.outputports.FindAllRoomsOutputPort;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.domain.RoomStatusEnum;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllRoomsAdapter implements FindAllRoomsOutputPort {

    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoomWithHotelDTO> findAll() {
        var rows = roomRepository.findAllProjectedBy(); // List<RoomRepository.RoomWithHotelId>

        // Map interface-projection -> DTO. Adjust conversions if your projection types differ.
        return rows.stream()
            .map(r -> new RoomWithHotelDTO(
                r.getId(),
                r.getNumber(),
                RoomStatusEnum.valueOf(r.getStatus()),
                new BigDecimal(r.getPricePerNight()),
                r.getCapacity(),
                r.getHotelId(),
                r.getHotelName()
            ))
            .toList();
    }

}
