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
    void updateRestaurant_updatesScalarFields_thenMapsBack() {
        java.util.UUID id = java.util.UUID.randomUUID();
        Restaurant domain = new Restaurant(id, "NewName", "NewAddr", null);
        RestaurantEntity existing = new RestaurantEntity(id, "OldName", "OldAddr", null);
        RestaurantEntity saved = new RestaurantEntity(id, "NewName", "NewAddr", null);
        Restaurant mappedBack = new Restaurant(id, "NewName", "NewAddr", null);

        when(restaurantRepository.findById(id.toString())).thenReturn(java.util.Optional.of(existing));
        when(restaurantRepository.save(existing)).thenReturn(saved);
        when(restaurantRepositoryMapper.toDomain(saved)).thenReturn(mappedBack);

        Restaurant result = adapter.updateRestaurant(domain);

        assertEquals(mappedBack, result);
        verify(restaurantRepository).findById(id.toString());
        verify(restaurantRepository).save(existing);
        verify(restaurantRepositoryMapper).toDomain(saved);
    }
}
