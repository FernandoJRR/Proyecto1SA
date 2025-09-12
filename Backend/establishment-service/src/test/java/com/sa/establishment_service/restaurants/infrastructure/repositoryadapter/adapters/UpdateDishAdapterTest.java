package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.mappers.DishRepositoryMapper;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.repositories.DishRepository;

class UpdateDishAdapterTest {

    @Mock private DishRepository dishRepository;
    @Mock private DishRepositoryMapper dishRepositoryMapper;

    private UpdateDishAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UpdateDishAdapter(dishRepository, dishRepositoryMapper);
    }

    @Test
    void updateDish_mapsAndPersists_thenMapsBack() {
        UUID id = UUID.randomUUID();
        Dish domain = new Dish(id, "Old", new BigDecimal("5.00"));
        DishEntity existing = new DishEntity(id, "Old", new BigDecimal("5.00"));
        DishEntity saved = new DishEntity(id, "New", new BigDecimal("6.50"));
        Dish mappedBack = new Dish(id, "New", new BigDecimal("6.50"));

        when(dishRepository.findById(id.toString())).thenReturn(Optional.of(existing));
        when(dishRepository.save(existing)).thenReturn(saved);
        when(dishRepositoryMapper.toDomain(saved)).thenReturn(mappedBack);

        Dish result = adapter.updateDish(domain);

        assertEquals(mappedBack, result);
        verify(dishRepository).findById(id.toString());
        verify(dishRepository).save(existing);
        verify(dishRepositoryMapper).toDomain(saved);
    }
}

