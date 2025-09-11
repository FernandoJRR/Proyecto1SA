package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;

class CreateHotelAdapterTest {

    @Mock private HotelRepository hotelRepository;
    @Mock private HotelRepositoryMapper hotelRepositoryMapper;

    private CreateHotelAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new CreateHotelAdapter(hotelRepository, hotelRepositoryMapper);
    }

    @Test
    void createHotel_mapsAndPersists_thenMapsBack() {
        Hotel domain = Hotel.create("H", "A", new BigDecimal("10.50"));
        HotelEntity entity = new HotelEntity(null, "H", "A", new BigDecimal("10.50"));
        HotelEntity savedEntity = new HotelEntity("id-1", "H", "A", new BigDecimal("10.50"));
        Hotel mappedBack = Hotel.create("H", "A", new BigDecimal("10.50"));

        when(hotelRepositoryMapper.toEntity(domain)).thenReturn(entity);
        when(hotelRepository.save(entity)).thenReturn(savedEntity);
        when(hotelRepositoryMapper.toDomain(savedEntity)).thenReturn(mappedBack);

        Hotel result = adapter.createHotel(domain);

        assertEquals(mappedBack, result);
        verify(hotelRepositoryMapper).toEntity(domain);
        verify(hotelRepository).save(entity);
        verify(hotelRepositoryMapper).toDomain(savedEntity);
    }
}

