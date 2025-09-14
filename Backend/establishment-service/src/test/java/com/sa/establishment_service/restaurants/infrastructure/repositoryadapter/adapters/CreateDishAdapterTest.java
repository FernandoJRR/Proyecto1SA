package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;

import com.sa.establishment_service.restaurants.application.outputports.CreateDishOutputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.RestaurantRepository;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CreateDishAdapterTest {

    @Mock
    private DishRepositoryMapper dishRepositoryMapper;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private CreateDishAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new CreateDishAdapter(dishRepositoryMapper, dishRepository, restaurantRepository);
    }

    @Test
    void createDish_whenRestaurantExists_savesDishAndReturnsDomain() throws NotFoundException {
        // Arrange
        String restaurantId = UUID.randomUUID().toString();
        UUID dishId = UUID.randomUUID();
        Dish domainDish = new Dish(dishId, "New Dish", new BigDecimal("10.50"));
        DishEntity dishEntityBeforeSave = new DishEntity(dishId, "New Dish", new BigDecimal("10.50"));
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(restaurantId);
        // mapper should convert domain to entity
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(dishRepositoryMapper.toEntity(domainDish)).thenReturn(dishEntityBeforeSave);
        // simulate save
        DishEntity dishEntitySaved = new DishEntity(dishId, "New Dish", new BigDecimal("10.50"), restaurantEntity);
        when(dishRepository.save(dishEntityBeforeSave)).thenReturn(dishEntitySaved);
        // mapper should convert entity back to domain
        Dish expectedDomain = new Dish(dishId, "New Dish", new BigDecimal("10.50"));
        when(dishRepositoryMapper.toDomain(dishEntitySaved)).thenReturn(expectedDomain);

        // Act
        Dish result = adapter.createDish(restaurantId, domainDish);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDomain, result);
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(dishRepositoryMapper, times(1)).toEntity(domainDish);
        verify(dishRepository).save(dishEntityBeforeSave);
        verify(dishRepositoryMapper, times(1)).toDomain(dishEntitySaved);
    }

    @Test
    void createDish_whenRestaurantNotFound_throwsNotFoundException() {
        // Arrange
        String restaurantId = UUID.randomUUID().toString();
        Dish domainDish = new Dish(UUID.randomUUID(), "Some Dish", new BigDecimal("8.00"));

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            adapter.createDish(restaurantId, domainDish);
        });
        assertEquals("Restaurante no encontrado", ex.getMessage());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verifyNoInteractions(dishRepositoryMapper, dishRepository);
    }
}