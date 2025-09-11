package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.sa.client_service.orders.application.outputports.FindOrderByIdOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindOrderByIdAdapter implements FindOrderByIdOutputPort {

    private final OrderRepository orderRepository;
    private final OrderRepositoryMapper orderRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(String orderId) {
        return orderRepository.findById(orderId)
            .map(orderRepositoryMapper::toDomain);
    }

}
