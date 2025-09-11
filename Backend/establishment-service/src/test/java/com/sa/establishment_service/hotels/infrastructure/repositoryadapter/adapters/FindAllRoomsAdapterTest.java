package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.dtos.RoomWithHotelDTO;
import com.sa.establishment_service.hotels.domain.RoomStatusEnum;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;

class FindAllRoomsAdapterTest {

    private RoomRepository roomRepository;
    private FindAllRoomsAdapter adapter;

    @BeforeEach
    void setUp() {
        roomRepository = mock(RoomRepository.class);
        adapter = new FindAllRoomsAdapter(roomRepository);
    }

    @Test
    void findAll_mapsProjectionToDTO() {
        RoomRepository.RoomWithHotelId p1 = mock(RoomRepository.RoomWithHotelId.class);
        when(p1.getId()).thenReturn("r1");
        when(p1.getNumber()).thenReturn("101");
        when(p1.getStatus()).thenReturn("AVAILABLE");
        when(p1.getPricePerNight()).thenReturn("50.00");
        when(p1.getCapacity()).thenReturn(2);
        when(p1.getHotelId()).thenReturn("h1");
        when(p1.getHotelName()).thenReturn("H1");

        RoomRepository.RoomWithHotelId p2 = mock(RoomRepository.RoomWithHotelId.class);
        when(p2.getId()).thenReturn("r2");
        when(p2.getNumber()).thenReturn("102");
        when(p2.getStatus()).thenReturn("UNAVAILABLE");
        when(p2.getPricePerNight()).thenReturn("75.50");
        when(p2.getCapacity()).thenReturn(3);
        when(p2.getHotelId()).thenReturn("h2");
        when(p2.getHotelName()).thenReturn("H2");

        when(roomRepository.findAllProjectedBy()).thenReturn(List.of(p1, p2));

        List<RoomWithHotelDTO> result = adapter.findAll();

        assertEquals(2, result.size());
        RoomWithHotelDTO d1 = result.get(0);
        assertEquals("r1", d1.getId());
        assertEquals("101", d1.getNumber());
        assertEquals(RoomStatusEnum.AVAILABLE, d1.getStatus());
        assertEquals("50.00", d1.getPricePerNight().toString());
        assertEquals(2, d1.getCapacity());
        assertEquals("h1", d1.getHotelId());
        assertEquals("H1", d1.getHotelName());

        RoomWithHotelDTO d2 = result.get(1);
        assertEquals("r2", d2.getId());
        assertEquals(RoomStatusEnum.UNAVAILABLE, d2.getStatus());
        assertEquals("75.50", d2.getPricePerNight().toString());
    }
}

