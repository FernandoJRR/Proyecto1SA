package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.establishment_service.hotels.domain.Hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CreateRestaurantAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantRepositoryMapper restaurantRepositoryMapper;

    private CreateRestaurantAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new CreateRestaurantAdapter(restaurantRepository, restaurantRepositoryMapper);
    }

    @Test
    void createRestaurant_savesAndReturnsDomainRestaurant() {
        // Arrange
        UUID restaurantId = UUID.randomUUID();
        Hotel hotel = mock(Hotel.class);  // mock or create a real Hotel if needed
        Restaurant domainRestaurant = new Restaurant(restaurantId, "My Restaurant", "123 Main St", hotel);

        RestaurantEntity entityToSave = new RestaurantEntity();
        entityToSave.setId(restaurantId.toString());
        entityToSave.setName(domainRestaurant.getName());
        entityToSave.setAddress(domainRestaurant.getAddress());
        // Could set hotel id or property on entityToSave if mapping includes hotel

        RestaurantEntity savedEntity = new RestaurantEntity();
        savedEntity.setId(restaurantId.toString());
        savedEntity.setName(domainRestaurant.getName());
        savedEntity.setAddress(domainRestaurant.getAddress());
        // Similarly set hotel in savedEntity if needed

        Restaurant expectedDomainFromSaved = new Restaurant(restaurantId, "My Restaurant", "123 Main St", hotel);

        when(restaurantRepositoryMapper.toEntity(domainRestaurant)).thenReturn(entityToSave);
        when(restaurantRepository.save(entityToSave)).thenReturn(savedEntity);
        when(restaurantRepositoryMapper.toDomain(savedEntity)).thenReturn(expectedDomainFromSaved);

        // Act
        Restaurant result = adapter.createRestaurant(domainRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDomainFromSaved, result);
        verify(restaurantRepositoryMapper, times(1)).toEntity(domainRestaurant);
        verify(restaurantRepository, times(1)).save(entityToSave);
        verify(restaurantRepositoryMapper, times(1)).toDomain(savedEntity);
    }
}