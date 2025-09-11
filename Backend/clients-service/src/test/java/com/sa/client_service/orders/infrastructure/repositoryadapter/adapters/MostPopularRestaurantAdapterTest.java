package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class MostPopularRestaurantAdapterTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderRepositoryMapper orderRepositoryMapper;

    private MostPopularRestaurantAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new MostPopularRestaurantAdapter(orderRepository, orderRepositoryMapper);
    }

    @Test
    void whenNoResults_thenReturnsEmpty() {
        when(orderRepository.findTopRestaurants(any(Pageable.class)))
            .thenReturn(Collections.emptyList());

        Optional<Order> result = adapter.getMostPopular();

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findTopRestaurants(any(Pageable.class));
    }

    @Test
    void whenResults_thenMapsFirstAndReturns() {
        OrderEntity entity = new OrderEntity();
        when(orderRepository.findTopRestaurants(any(Pageable.class)))
            .thenReturn(List.of(entity));
        Order mapped = org.mockito.Mockito.mock(Order.class);
        when(orderRepositoryMapper.toDomain(entity)).thenReturn(mapped);

        Optional<Order> result = adapter.getMostPopular();

        assertTrue(result.isPresent());
        assertSame(mapped, result.get());
        verify(orderRepositoryMapper, times(1)).toDomain(entity);
    }
}

