package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class MostPopularDishesAdapterTest {

    @Mock private OrderRepository orderRepository;

    private MostPopularDishesAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new MostPopularDishesAdapter(orderRepository);
    }

    @Test
    void givenRestaurantId_whenQuery_thenDelegatesAndReturnsList() {
        UUID restaurantId = UUID.randomUUID();
        List<UUID> expected = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(orderRepository.findTopDishesByRestaurant(restaurantId)).thenReturn(expected);

        List<UUID> result = adapter.mostPopularDishes(restaurantId);

        assertEquals(expected, result);
        verify(orderRepository, times(1)).findTopDishesByRestaurant(restaurantId);
    }
}

