package com.sa.client_service.orders.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;

import com.sa.client_service.orders.application.outputports.MostPopularDishesOutputPort;
import com.sa.client_service.orders.infrastructure.repositoryadapter.repositories.OrderRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MostPopularDishesAdapter implements MostPopularDishesOutputPort {

    private final OrderRepository orderRepository;

    @Override
    public List<UUID> mostPopularDishes(UUID restaurantId) {
        return orderRepository.findTopDishesByRestaurant(restaurantId);
    }

}
