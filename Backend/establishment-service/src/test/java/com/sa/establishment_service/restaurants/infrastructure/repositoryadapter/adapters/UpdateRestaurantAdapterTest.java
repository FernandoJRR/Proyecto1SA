package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;

class UpdateRestaurantAdapterTest {

    @Mock private RestaurantRepository restaurantRepository;
    @Mock private RestaurantRepositoryMapper restaurantRepositoryMapper;

    private UpdateRestaurantAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UpdateRestaurantAdapter(restaurantRepository, restaurantRepositoryMapper);
    }

    @Test
    void updateRestaurant_mapsAndPersists_thenMapsBack() {
        Restaurant domain = Restaurant.create("N", "A", null);
        java.util.UUID id = java.util.UUID.randomUUID();
        RestaurantEntity entity = new RestaurantEntity(id, "N", "A", null);
        RestaurantEntity savedEntity = new RestaurantEntity(id, "N2", "A2", null);
        Restaurant mappedBack = Restaurant.create("N2", "A2", null);

        when(restaurantRepositoryMapper.toEntity(domain)).thenReturn(entity);
        when(restaurantRepository.save(entity)).thenReturn(savedEntity);
        when(restaurantRepositoryMapper.toDomain(savedEntity)).thenReturn(mappedBack);

        Restaurant result = adapter.updateRestaurant(domain);

        assertEquals(mappedBack, result);
        verify(restaurantRepositoryMapper).toEntity(domain);
        verify(restaurantRepository).save(entity);
        verify(restaurantRepositoryMapper).toDomain(savedEntity);
    }
}
