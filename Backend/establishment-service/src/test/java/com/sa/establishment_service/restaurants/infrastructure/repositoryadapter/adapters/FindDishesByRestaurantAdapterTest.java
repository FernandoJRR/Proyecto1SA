package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindDishesByRestaurantAdapterTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishRepositoryMapper dishRepositoryMapper;

    private FindDishesByRestaurantAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindDishesByRestaurantAdapter(dishRepository, dishRepositoryMapper);
    }

    @Test
    void findByRestaurant_whenDishesExist_returnsListOfDomainDishes() {
        // Arrange
        String restaurantId = UUID.randomUUID().toString();

        // Prepare some DishEntity list
        UUID dishId1 = UUID.randomUUID();
        DishEntity entity1 = new DishEntity();
        entity1.setId(dishId1.toString());
        entity1.setName("Tacos");
        entity1.setPrice(new BigDecimal("12.50"));

        UUID dishId2 = UUID.randomUUID();
        DishEntity entity2 = new DishEntity();
        entity2.setId(dishId2.toString());
        entity2.setName("Burrito");
        entity2.setPrice(new BigDecimal("8.75"));

        List<DishEntity> entityList = List.of(entity1, entity2);

        // Prepare expected domain list
        Dish domain1 = new Dish(UUID.fromString(entity1.getId()), entity1.getName(), entity1.getPrice());
        Dish domain2 = new Dish(UUID.fromString(entity2.getId()), entity2.getName(), entity2.getPrice());
        List<Dish> expectedList = List.of(domain1, domain2);

        when(dishRepository.findAllByRestaurant_Id(restaurantId)).thenReturn(entityList);
        when(dishRepositoryMapper.toDomain(entityList)).thenReturn(expectedList);

        // Act
        List<Dish> result = adapter.findByRestaurant(restaurantId);

        // Assert
        assertNotNull(result, "Expected result not null");
        assertEquals(2, result.size(), "Expected two dishes");
        assertEquals(expectedList, result, "Expected mapped domain dishes list");
        verify(dishRepository).findAllByRestaurant_Id(restaurantId);
        verify(dishRepositoryMapper).toDomain(entityList);
    }

    @Test
    void findByRestaurant_whenNoDishes_returnsEmptyList() {
        // Arrange
        String restaurantId = UUID.randomUUID().toString();

        when(dishRepository.findAllByRestaurant_Id(restaurantId)).thenReturn(List.of());
        when(dishRepositoryMapper.toDomain(List.of())).thenReturn(List.of());

        // Act
        List<Dish> result = adapter.findByRestaurant(restaurantId);

        // Assert
        assertNotNull(result, "Expected result not null even if empty");
        assertTrue(result.isEmpty(), "Expected no dishes");
        verify(dishRepository).findAllByRestaurant_Id(restaurantId);
        verify(dishRepositoryMapper).toDomain(List.of());
    }
}