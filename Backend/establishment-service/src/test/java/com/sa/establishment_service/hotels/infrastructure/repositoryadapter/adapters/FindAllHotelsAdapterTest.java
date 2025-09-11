package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;

class FindAllHotelsAdapterTest {

    @Mock private HotelRepository hotelRepository;
    @Mock private HotelRepositoryMapper hotelRepositoryMapper;
    private FindAllHotelsAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindAllHotelsAdapter(hotelRepository, hotelRepositoryMapper);
    }

    @Test
    void findAll_mapsEntitiesToDomain() {
        List<HotelEntity> entities = List.of(
            new HotelEntity("h1", "H1", "A1", new BigDecimal("10")),
            new HotelEntity("h2", "H2", "A2", new BigDecimal("20"))
        );
        List<Hotel> domain = List.of(
            Hotel.create("H1", "A1", new BigDecimal("10")),
            Hotel.create("H2", "A2", new BigDecimal("20"))
        );
        when(hotelRepository.findAll()).thenReturn(entities);
        when(hotelRepositoryMapper.toDomainList(entities)).thenReturn(domain);

        List<Hotel> result = adapter.findAll();
        assertEquals(domain, result);
        assertEquals(2, result.size());
    }
}

