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

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;

class UpdateHotelAdapterTest {

    @Mock private HotelRepository hotelRepository;
    @Mock private HotelRepositoryMapper hotelRepositoryMapper;

    private UpdateHotelAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UpdateHotelAdapter(hotelRepository, hotelRepositoryMapper);
    }

    @Test
    void updateHotel_mapsAndPersists_thenMapsBack() {
        UUID id = UUID.randomUUID();
        Hotel domain = new Hotel(id, "N", "A", new BigDecimal("10.00"));
        HotelEntity entity = new HotelEntity(id.toString(), "N", "A", new BigDecimal("10.00"));
        HotelEntity savedEntity = new HotelEntity(id.toString(), "N2", "A2", new BigDecimal("11.00"));
        Hotel mappedBack = new Hotel(id, "N2", "A2", new BigDecimal("11.00"));

        when(hotelRepositoryMapper.toEntity(domain)).thenReturn(entity);
        when(hotelRepository.save(entity)).thenReturn(savedEntity);
        when(hotelRepositoryMapper.toDomain(savedEntity)).thenReturn(mappedBack);

        Hotel result = adapter.updateHotel(domain);

        assertEquals(mappedBack, result);
        verify(hotelRepositoryMapper).toEntity(domain);
        verify(hotelRepository).save(entity);
        verify(hotelRepositoryMapper).toDomain(savedEntity);
    }
}

