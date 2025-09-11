package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
public class FindOrderByIdAdapterTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderRepositoryMapper orderRepositoryMapper;

    private FindOrderByIdAdapter adapter;

    private static final String ORDER_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setup() {
        adapter = new FindOrderByIdAdapter(orderRepository, orderRepositoryMapper);
    }

    @Test
    void givenExistingOrder_whenFindById_thenReturnsDomainOptional() {
        OrderEntity entity = new OrderEntity();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(entity));
        Order domain = org.mockito.Mockito.mock(Order.class);
        when(orderRepositoryMapper.toDomain(same(entity))).thenReturn(domain);

        Optional<Order> result = adapter.findById(ORDER_ID);

        assertTrue(result.isPresent());
        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepositoryMapper, times(1)).toDomain(same(entity));
    }

    @Test
    void givenMissingOrder_whenFindById_thenReturnsEmptyAndNoMap() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        Optional<Order> result = adapter.findById(ORDER_ID);

        assertFalse(result.isPresent());
        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepositoryMapper, never()).toDomain(any(OrderEntity.class));
    }
}

