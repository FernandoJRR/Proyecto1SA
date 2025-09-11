package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class FindOrdersByRestaurantAdapterTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderRepositoryMapper orderRepositoryMapper;

    private FindOrdersByRestaurantAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindOrdersByRestaurantAdapter(orderRepository, orderRepositoryMapper);
    }

    @Test
    void givenRestaurantId_whenFind_thenConvertsUuidDelegatesAndMapsList() {
        String restaurantId = UUID.randomUUID().toString();
        List<OrderEntity> entities = List.of(new OrderEntity());
        when(orderRepository.findByRestaurantId(any(UUID.class))).thenReturn(entities);
        List<Order> mapped = List.of(org.mockito.Mockito.mock(Order.class));
        when(orderRepositoryMapper.toDomain(entities)).thenReturn(mapped);

        List<Order> result = adapter.findByRestaurant(restaurantId);

        assertEquals(mapped, result);
        verify(orderRepository, times(1)).findByRestaurantId(any(UUID.class));
        verify(orderRepositoryMapper, times(1)).toDomain(entities);
    }
}

