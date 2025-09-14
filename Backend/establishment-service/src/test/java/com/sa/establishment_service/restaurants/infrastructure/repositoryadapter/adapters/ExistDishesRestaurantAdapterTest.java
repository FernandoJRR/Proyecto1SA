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

class ExistDishesRestaurantAdapterTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishRepositoryMapper dishRepositoryMapper;

    private ExistDishesRestaurantAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistDishesRestaurantAdapter(dishRepository, dishRepositoryMapper);
    }

    @Test
    void findDishesByRestaurantAndIds_whenSomeExist_returnsMappedDomainDishes() {
        // Arrange
        String restaurantId = UUID.randomUUID().toString();
        List<String> presentIds = List.of(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );

        // create DishEntity list returned by repository
        DishEntity e1 = new DishEntity();
        e1.setId(presentIds.get(0));
        e1.setName("Dish A");
        e1.setPrice(new BigDecimal("5.00"));

        DishEntity e2 = new DishEntity();
        e2.setId(presentIds.get(1));
        e2.setName("Dish B");
        e2.setPrice(new BigDecimal("7.50"));

        List<DishEntity> entityList = List.of(e1, e2);

        // domain list (expected)
        Dish d1 = new Dish(UUID.fromString(e1.getId()), e1.getName(), e1.getPrice());
        Dish d2 = new Dish(UUID.fromString(e2.getId()), e2.getName(), e2.getPrice());
        List<Dish> expectedList = List.of(d1, d2);

        when(dishRepository.findExistant(restaurantId, presentIds)).thenReturn(entityList);
        when(dishRepositoryMapper.toDomain(entityList)).thenReturn(expectedList);

        // Act
        List<Dish> result = adapter.findDishesByRestaurantAndIds(restaurantId, presentIds);

        // Assert
        assertNotNull(result);
        assertEquals(expectedList, result);
        verify(dishRepository).findExistant(restaurantId, presentIds);
        verify(dishRepositoryMapper).toDomain(entityList);
    }

    @Test
    void findDishesByRestaurantAndIds_whenNoneExist_returnsEmptyList() {
        // Arrange
        String restaurantId = UUID.randomUUID().toString();
        List<String> presentIds = List.of(UUID.randomUUID().toString());

        when(dishRepository.findExistant(restaurantId, presentIds)).thenReturn(List.of());
        when(dishRepositoryMapper.toDomain(List.of())).thenReturn(List.of());

        // Act
        List<Dish> result = adapter.findDishesByRestaurantAndIds(restaurantId, presentIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dishRepository).findExistant(restaurantId, presentIds);
        verify(dishRepositoryMapper).toDomain(List.of());
    }
}