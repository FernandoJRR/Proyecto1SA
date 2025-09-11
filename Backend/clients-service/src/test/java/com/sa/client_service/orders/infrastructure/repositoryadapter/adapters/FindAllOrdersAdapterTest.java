package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.orders.application.dtos.FindOrdersDTO;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class FindAllOrdersAdapterTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderRepositoryMapper orderRepositoryMapper;

    private FindAllOrdersAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindAllOrdersAdapter(orderRepository, orderRepositoryMapper);
    }

    @Test
    void givenFilter_whenFindAll_thenDelegatesMapsAndReturnsList() {
        FindOrdersDTO filter = new FindOrdersDTO(LocalDate.now().minusDays(10), LocalDate.now());

        List<OrderEntity> entities = List.of(new OrderEntity(), new OrderEntity());
        when(orderRepository.findAll(any(Specification.class))).thenReturn(entities);

        List<Order> mapped = List.of(org.mockito.Mockito.mock(Order.class), org.mockito.Mockito.mock(Order.class));
        when(orderRepositoryMapper.toDomain(entities)).thenReturn(mapped);

        List<Order> result = adapter.findAll(filter);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(mapped));
        verify(orderRepository, times(1)).findAll(any(Specification.class));
        verify(orderRepositoryMapper, times(1)).toDomain(entities);
    }

    @Test
    void givenEmptyResult_whenFindAll_thenReturnsEmpty() {
        FindOrdersDTO filter = new FindOrdersDTO(null, null);
        when(orderRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<Order> result = adapter.findAll(filter);

        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll(any(Specification.class));
    }
}

