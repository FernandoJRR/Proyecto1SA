package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.sa.client_service.orders.application.outputports.FindOrdersByRestaurantOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindOrdersByRestaurantAdapter implements FindOrdersByRestaurantOutputPort {

    private final OrderRepository orderRepository;
    private final OrderRepositoryMapper orderRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByRestaurant(String restaurantId) {
        return orderRepositoryMapper.toDomain(orderRepository.findByRestaurantId(UUID.fromString(restaurantId)));
    }

}
