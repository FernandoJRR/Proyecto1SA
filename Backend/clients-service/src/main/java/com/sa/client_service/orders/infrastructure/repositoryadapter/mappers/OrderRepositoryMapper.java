package com.sa.client_service.orders.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderRepositoryMapper {
    @Mapping(target = "client.reviews", ignore = true)
    public OrderEntity toEntity(Order order);

    public Order toDomain(OrderEntity orderEntity);
    public List<Order> toDomain(List<OrderEntity> orderEntity);
}
