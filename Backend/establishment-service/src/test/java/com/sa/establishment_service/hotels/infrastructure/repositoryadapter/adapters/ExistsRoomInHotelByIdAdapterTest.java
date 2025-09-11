package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;

class ExistsRoomInHotelByIdAdapterTest {

    @Mock private RoomRepository roomRepository;
    private ExistsRoomInHotelByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistsRoomInHotelByIdAdapter(roomRepository);
    }

    @Test
    void existsRoomInHotel_trueWhenRoomBelongsToHotel() {
        String hotelId = "h1";
        String roomId = "r1";
        HotelEntity hotel = new HotelEntity(hotelId, "H", "A", new BigDecimal("10"));
        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setHotel(hotel);

        when(roomRepository.existsById(roomId)).thenReturn(true);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        assertTrue(adapter.existsRoomInHotel(hotelId, roomId));
    }

    @Test
    void existsRoomInHotel_falseWhenRoomNotExistsOrDifferentHotel() {
        when(roomRepository.existsById("rX")).thenReturn(false);
        assertFalse(adapter.existsRoomInHotel("h1", "rX"));

        String hotelId = "h1";
        String roomId = "r2";
        HotelEntity anotherHotel = new HotelEntity("h2", "H2", "A2", new BigDecimal("10"));
        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setHotel(anotherHotel);

        when(roomRepository.existsById(roomId)).thenReturn(true);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        assertFalse(adapter.existsRoomInHotel(hotelId, roomId));
    }
}

