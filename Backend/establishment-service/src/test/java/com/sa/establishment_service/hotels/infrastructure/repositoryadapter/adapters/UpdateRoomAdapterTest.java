package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.domain.RoomStatusEnum;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;

class UpdateRoomAdapterTest {

    @Mock private RoomRepository roomRepository;
    @Mock private RoomRepositoryMapper roomRepositoryMapper;

    private UpdateRoomAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UpdateRoomAdapter(roomRepository, roomRepositoryMapper);
    }

    @Test
    void updateRoom_mapsAndPersists_thenMapsBack() {
        UUID id = UUID.randomUUID();
        Room domain = new Room(id, "101", new BigDecimal("50.00"), 2, RoomStatusEnum.AVAILABLE);
        HotelEntity hotel = new HotelEntity("h1", "H", "A", new BigDecimal("10.00"));
        RoomEntity existing = new RoomEntity(id.toString(), "101", new BigDecimal("50.00"), 2, hotel);
        RoomEntity saved = new RoomEntity(id.toString(), "102", new BigDecimal("60.50"), 3, hotel);
        Room mappedBack = new Room(id, "102", new BigDecimal("60.50"), 3, RoomStatusEnum.AVAILABLE);

        when(roomRepository.findById(id.toString())).thenReturn(java.util.Optional.of(existing));
        when(roomRepository.save(existing)).thenReturn(saved);
        when(roomRepositoryMapper.toDomain(saved)).thenReturn(mappedBack);

        Room result = adapter.updateRoom(domain);

        assertEquals(mappedBack, result);
        verify(roomRepository).findById(id.toString());
        verify(roomRepository).save(existing);
        verify(roomRepositoryMapper).toDomain(saved);
    }
}
