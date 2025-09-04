package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import java.util.List;

import com.sa.client_service.orders.application.outputports.FindAllOrdersOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllOrdersAdapter implements FindAllOrdersOutputPort {

    private final OrderRepository orderRepository;
    private final OrderRepositoryMapper orderRepositoryMapper;

    @Override
    public List<Order> findAll() {
        List<OrderEntity> result = orderRepository.findAll();
        return orderRepositoryMapper.toDomain(result);
    }

}
