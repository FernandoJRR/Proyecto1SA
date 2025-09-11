package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderItemEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class CreateOrderAdapterTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderRepositoryMapper orderRepositoryMapper;

    private CreateOrderAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new CreateOrderAdapter(orderRepository, orderRepositoryMapper);
    }

    @Test
    void givenOrderWithItems_whenCreate_thenSetsBackRefsSavesAndMaps() {
        // given
        Order domain = org.mockito.Mockito.mock(Order.class);

        OrderEntity entity = new OrderEntity();
        OrderItemEntity it1 = new OrderItemEntity();
        OrderItemEntity it2 = new OrderItemEntity();
        entity.setItems(Arrays.asList(it1, it2));

        when(orderRepositoryMapper.toEntity(org.mockito.ArgumentMatchers.same(domain)))
            .thenReturn(entity);
        when(orderRepository.save(org.mockito.ArgumentMatchers.same(entity)))
            .thenReturn(entity);
        Order mapped = org.mockito.Mockito.mock(Order.class);
        when(orderRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entity)))
            .thenReturn(mapped);

        // when
        Order result = adapter.createOrder(domain);

        // then
        assertSame(mapped, result);
        assertSame(entity, it1.getOrder());
        assertSame(entity, it2.getOrder());
        verify(orderRepositoryMapper, times(1)).toEntity(org.mockito.ArgumentMatchers.same(domain));
        verify(orderRepository, times(1)).save(org.mockito.ArgumentMatchers.same(entity));
        verify(orderRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entity));
    }

    @Test
    void givenOrderWithNullItems_whenCreate_thenSavesAndMaps() {
        Order domain = org.mockito.Mockito.mock(Order.class);
        OrderEntity entity = new OrderEntity();
        entity.setItems(null);

        when(orderRepositoryMapper.toEntity(org.mockito.ArgumentMatchers.same(domain)))
            .thenReturn(entity);
        when(orderRepository.save(org.mockito.ArgumentMatchers.same(entity)))
            .thenReturn(entity);
        Order mapped = org.mockito.Mockito.mock(Order.class);
        when(orderRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entity)))
            .thenReturn(mapped);

        Order result = adapter.createOrder(domain);

        assertSame(mapped, result);
        verify(orderRepositoryMapper, times(1)).toEntity(org.mockito.ArgumentMatchers.same(domain));
        verify(orderRepository, times(1)).save(org.mockito.ArgumentMatchers.same(entity));
        verify(orderRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entity));
    }
}

