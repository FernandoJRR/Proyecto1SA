package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;

class FindAllRoomsByHotelIdAdapterTest {

    @Mock private RoomRepository roomRepository;
    @Mock private RoomRepositoryMapper roomRepositoryMapper;
    private FindAllRoomsByHotelIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindAllRoomsByHotelIdAdapter(roomRepository, roomRepositoryMapper);
    }

    @Test
    void findByHotelId_mapsList() {
        UUID hotelId = UUID.randomUUID();
        List<RoomEntity> entities = List.of(new RoomEntity("r1", "101", new BigDecimal("50"), 2, null));
        List<Room> domain = List.of(Room.create("101", new BigDecimal("50"), 2));

        when(roomRepository.findAllByHotel_Id(hotelId.toString())).thenReturn(entities);
        when(roomRepositoryMapper.toDomainList(entities)).thenReturn(domain);

        List<Room> result = adapter.findByHotelId(hotelId);

        assertEquals(domain, result);
        assertEquals(1, result.size());
    }
}

