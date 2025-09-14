package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.RestaurantRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindRestaurantByHotelAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantRepositoryMapper restaurantRepositoryMapper;

    private FindRestaurantByHotelAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindRestaurantByHotelAdapter(restaurantRepository, restaurantRepositoryMapper);
    }

    @Test
    void findByHotel_whenRestaurantsExist_returnsListOfDomainRestaurants() {
        // Arrange
        String hotelId = UUID.randomUUID().toString();

        RestaurantEntity entity1 = new RestaurantEntity();
        entity1.setId(UUID.randomUUID().toString());
        entity1.setName("Rest A");
        entity1.setAddress("Address A");

        RestaurantEntity entity2 = new RestaurantEntity();
        entity2.setId(UUID.randomUUID().toString());
        entity2.setName("Rest B");
        entity2.setAddress("Address B");

        List<RestaurantEntity> entityList = List.of(entity1, entity2);

        Restaurant domain1 = new Restaurant(UUID.fromString(entity1.getId()), entity1.getName(), entity1.getAddress(), null);
        Restaurant domain2 = new Restaurant(UUID.fromString(entity2.getId()), entity2.getName(), entity2.getAddress(), null);
        List<Restaurant> expectedList = List.of(domain1, domain2);

        when(restaurantRepository.findAllByHotel_Id(hotelId)).thenReturn(entityList);
        when(restaurantRepositoryMapper.toDomain(entityList)).thenReturn(expectedList);

        // Act
        List<Restaurant> result = adapter.findByHotel(hotelId);

        // Assert
        assertNotNull(result, "Expected result not null");
        assertEquals(expectedList, result, "Expected mapped domain restaurants list");
        assertEquals(2, result.size());

        verify(restaurantRepository).findAllByHotel_Id(hotelId);
        verify(restaurantRepositoryMapper).toDomain(entityList);
    }

    @Test
    void findByHotel_whenNoRestaurants_returnsEmptyList() {
        // Arrange
        String hotelId = UUID.randomUUID().toString();
        when(restaurantRepository.findAllByHotel_Id(hotelId)).thenReturn(List.of());
        when(restaurantRepositoryMapper.toDomain(List.of())).thenReturn(List.of());

        // Act
        List<Restaurant> result = adapter.findByHotel(hotelId);

        // Assert
        assertNotNull(result, "Expected non-null result even if empty");
        assertTrue(result.isEmpty(), "Expected empty list");
        verify(restaurantRepository).findAllByHotel_Id(hotelId);
        verify(restaurantRepositoryMapper).toDomain(List.of());
    }
}