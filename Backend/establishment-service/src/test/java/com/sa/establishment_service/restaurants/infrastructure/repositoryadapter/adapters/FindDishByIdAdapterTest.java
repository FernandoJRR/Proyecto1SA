package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindDishByIdAdapterTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishRepositoryMapper dishRepositoryMapper;

    private FindDishByIdAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindDishByIdAdapter(dishRepository, dishRepositoryMapper);
    }

    @Test
    void findById_whenDishExists_returnsOptionalWithDish() {
        // Arrange
        String dishId = UUID.randomUUID().toString();
        DishEntity entity = new DishEntity();
        entity.setId(dishId);
        entity.setName("Tacos");
        entity.setPrice(new BigDecimal("12.50"));

        Dish domain = new Dish(UUID.fromString(dishId), "Tacos", new BigDecimal("12.50"));

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(entity));
        when(dishRepositoryMapper.toDomain(entity)).thenReturn(domain);

        // Act
        Optional<Dish> result = adapter.findById(dishId);

        // Assert
        assertTrue(result.isPresent(), "Expected dish to be found");
        assertEquals(domain, result.get(), "Expected domain dish matches mapped domain");
        verify(dishRepository).findById(dishId);
        verify(dishRepositoryMapper).toDomain(entity);
    }

    @Test
    void findById_whenDishNotFound_returnsEmptyOptional() {
        // Arrange
        String dishId = UUID.randomUUID().toString();
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        // Act
        Optional<Dish> result = adapter.findById(dishId);

        // Assert
        assertFalse(result.isPresent(), "Expected no dish to be found");
        verify(dishRepository).findById(dishId);
    }
}