package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindRestaurantByIdAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantRepositoryMapper restaurantRepositoryMapper;

    private FindRestaurantByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindRestaurantByIdAdapter(restaurantRepository, restaurantRepositoryMapper);
    }

    @Test
    void findById_whenRestaurantExists_returnsOptionalWithRestaurant() {
        // Arrange
        String id = UUID.randomUUID().toString();
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(id);
        entity.setName("La Buena Mesa");
        entity.setAddress("123 Calle Principal");

        Restaurant domain = new Restaurant(UUID.fromString(id), "La Buena Mesa", "123 Calle Principal", null);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(entity));
        when(restaurantRepositoryMapper.toDomain(entity)).thenReturn(domain);

        // Act
        Optional<Restaurant> result = adapter.findById(id);

        // Assert
        assertTrue(result.isPresent(), "Expected restaurant to be found");
        assertEquals(domain, result.get(), "Expected mapped domain restaurant");
        verify(restaurantRepository).findById(id);
        verify(restaurantRepositoryMapper).toDomain(entity);
    }

    @Test
    void findById_whenRestaurantNotFound_returnsEmptyOptional() {
        // Arrange
        String id = UUID.randomUUID().toString();
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurant> result = adapter.findById(id);

        // Assert
        assertFalse(result.isPresent(), "Expected no restaurant found");
        verify(restaurantRepository).findById(id);
    }
}