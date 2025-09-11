package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;

class FindHotelByIdAdapterTest {

    @Mock private HotelRepository hotelRepository;
    @Mock private HotelRepositoryMapper hotelRepositoryMapper;
    private FindHotelByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindHotelByIdAdapter(hotelRepository, hotelRepositoryMapper);
    }

    @Test
    void findHotelById_mapsWhenPresent() {
        UUID id = UUID.randomUUID();
        HotelEntity entity = new HotelEntity(id.toString(), "H", "A", new BigDecimal("10"));
        Hotel domain = Hotel.create("H", "A", new BigDecimal("10"));

        when(hotelRepository.findById(id.toString())).thenReturn(Optional.of(entity));
        when(hotelRepositoryMapper.toDomain(entity)).thenReturn(domain);

        var result = adapter.findHotelById(id);
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findHotelById_emptyWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(hotelRepository.findById(id.toString())).thenReturn(Optional.empty());
        var result = adapter.findHotelById(id);
        assertTrue(result.isEmpty());
    }
}

