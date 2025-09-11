package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.RoomRepository;

class ExistsRoomByIdAdapterTest {

    @Mock private RoomRepository roomRepository;
    private ExistsRoomByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistsRoomByIdAdapter(roomRepository);
    }

    @Test
    void existsById_returnsRepositoryValue() {
        when(roomRepository.existsById("r1")).thenReturn(true);
        when(roomRepository.existsById("r2")).thenReturn(false);

        assertTrue(adapter.existsById("r1"));
        assertFalse(adapter.existsById("r2"));
    }
}

