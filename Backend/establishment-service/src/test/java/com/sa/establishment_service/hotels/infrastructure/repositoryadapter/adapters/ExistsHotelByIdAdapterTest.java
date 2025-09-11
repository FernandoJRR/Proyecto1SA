package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;

class ExistsHotelByIdAdapterTest {

    @Mock private HotelRepository hotelRepository;
    private ExistsHotelByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistsHotelByIdAdapter(hotelRepository);
    }

    @Test
    void existsById_returnsRepositoryValue() {
        when(hotelRepository.existsById("h1")).thenReturn(true);
        when(hotelRepository.existsById("h2")).thenReturn(false);

        assertTrue(adapter.existsById("h1"));
        assertFalse(adapter.existsById("h2"));
    }
}

