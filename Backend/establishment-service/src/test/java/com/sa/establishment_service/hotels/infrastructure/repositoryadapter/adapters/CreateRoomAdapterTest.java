package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.RoomRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

class CreateRoomAdapterTest {

    @Mock private RoomRepository roomRepository;
    @Mock private HotelRepository hotelRepository;
    @Mock private RoomRepositoryMapper roomRepositoryMapper;

    private CreateRoomAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new CreateRoomAdapter(roomRepository, hotelRepository, roomRepositoryMapper);
    }

    @Test
    void createRoom_happyPath_persistsAndReturnsDomain() throws Exception {
        String hotelId = "h1";
        Room domainRoom = Room.create("101", new BigDecimal("50.00"), 2);

        HotelEntity hotelEntity = new HotelEntity(hotelId, "H", "A", new BigDecimal("10"));
        hotelEntity.setRooms(new ArrayList<>());
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));
        when(roomRepository.existsByHotel_IdAndNumber(hotelId, domainRoom.getNumber())).thenReturn(false);

        RoomEntity toSave = new RoomEntity();
        toSave.setNumber(domainRoom.getNumber());
        toSave.setPricePerNight(domainRoom.getPricePerNight());
        toSave.setCapacity(domainRoom.getCapacity());
        when(roomRepositoryMapper.toEntity(domainRoom)).thenReturn(toSave);

        RoomEntity saved = new RoomEntity();
        saved.setId("r-1");
        saved.setNumber(domainRoom.getNumber());
        saved.setPricePerNight(domainRoom.getPricePerNight());
        saved.setCapacity(domainRoom.getCapacity());
        saved.setHotel(hotelEntity);
        when(roomRepository.save(toSave)).thenReturn(saved);

        Room mappedBack = domainRoom; // reuse
        when(roomRepositoryMapper.toDomain(saved)).thenReturn(mappedBack);

        Room result = adapter.createRoom(hotelId, domainRoom);

        assertEquals(mappedBack, result);
        // ensure hotel was set on entity before save
        verify(roomRepository).save(toSave);
        assertEquals(hotelEntity, toSave.getHotel());
        // ensure hotel rooms collection was updated
        assertEquals(1, hotelEntity.getRooms().size());
        assertEquals(saved, hotelEntity.getRooms().get(0));
    }

    @Test
    void createRoom_throwsNotFound_whenHotelMissing() {
        when(hotelRepository.findById("h2")).thenReturn(Optional.empty());
        Room room = Room.create("102", new BigDecimal("60.00"), 2);
        assertThrows(NotFoundException.class, () -> adapter.createRoom("h2", room));
        verify(roomRepository, never()).save(any());
    }

    @Test
    void createRoom_throwsDuplicated_whenNumberExists() {
        String hotelId = "h3";
        HotelEntity hotelEntity = new HotelEntity(hotelId, "H", "A", new BigDecimal("10"));
        hotelEntity.setRooms(new ArrayList<>());
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));

        Room room = Room.create("103", new BigDecimal("60.00"), 2);
        when(roomRepository.existsByHotel_IdAndNumber(hotelId, room.getNumber())).thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> adapter.createRoom(hotelId, room));
        verify(roomRepository, never()).save(any());
    }
}

