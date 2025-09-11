package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;

class FindRoomByHotelAndIdAdapterTest {

    @Mock private RoomRepository roomRepository;
    @Mock private RoomRepositoryMapper roomRepositoryMapper;
    private FindRoomByHotelAndIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindRoomByHotelAndIdAdapter(roomRepository, roomRepositoryMapper);
    }

    @Test
    void findByHotelAndId_mapsWhenPresent() {
        String hotelId = "h1";
        String roomId = "r1";
        RoomEntity entity = new RoomEntity(roomId, "101", new BigDecimal("50"), 2, null);
        Room domain = Room.create("101", new BigDecimal("50"), 2);

        when(roomRepository.findOneByHotel_IdAndId(hotelId, roomId)).thenReturn(Optional.of(entity));
        when(roomRepositoryMapper.toDomain(entity)).thenReturn(domain);

        var result = adapter.findByHotelAndId(hotelId, roomId);
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findByHotelAndId_emptyWhenMissing() {
        String hotelId = "h2";
        String roomId = "r2";
        when(roomRepository.findOneByHotel_IdAndId(hotelId, roomId)).thenReturn(Optional.empty());
        var result = adapter.findByHotelAndId(hotelId, roomId);
        assertTrue(result.isEmpty());
    }
}

