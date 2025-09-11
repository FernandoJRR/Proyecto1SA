package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.sa.client_service.orders.application.outputports.MostPopularRestaurantOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.mappers.OrderRepositoryMapper;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MostPopularRestaurantAdapter implements MostPopularRestaurantOutputPort {

    private final OrderRepository orderRepository;
    private final OrderRepositoryMapper orderRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getMostPopular() {
        List<OrderEntity> orders = orderRepository.findTopRestaurants((Pageable) PageRequest.of(0, 1));
        if (orders.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(orderRepositoryMapper.toDomain(orders.get(0)));
        }
    }

}
