package com.sa.client_service.orders.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.repositoryadapter.models.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderRepositoryMapper {
    @Mapping(target = "client.reviews", ignore = true)
    public OrderEntity toEntity(Order order);

    public Order toDomain(OrderEntity orderEntity);
}
