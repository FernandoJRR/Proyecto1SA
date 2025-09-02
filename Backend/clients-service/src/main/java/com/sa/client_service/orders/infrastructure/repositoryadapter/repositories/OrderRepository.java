package com.sa.client_service.orders.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

}
