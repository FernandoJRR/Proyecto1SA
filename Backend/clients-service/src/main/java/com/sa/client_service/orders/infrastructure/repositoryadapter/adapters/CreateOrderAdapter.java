package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import com.sa.client_service.orders.application.outputports.CreateOrderOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderItemEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateOrderAdapter implements CreateOrderOutputPort {

    private final OrderRepository orderRepository;
    private final OrderRepositoryMapper orderRepositoryMapper;

    @Override
    public Order createOrder(Order order) {
        OrderEntity entity = orderRepositoryMapper.toEntity(order);

        if (entity.getItems() != null) {
            for (OrderItemEntity it : entity.getItems()) {
                it.setOrder(entity);
            }
        }

        OrderEntity saved = orderRepository.save(entity);
        return orderRepositoryMapper.toDomain(saved);
    }

}
