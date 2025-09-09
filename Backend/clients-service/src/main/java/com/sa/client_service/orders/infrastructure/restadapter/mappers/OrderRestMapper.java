package com.sa.client_service.orders.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.client_service.orders.application.dtos.CreateOrderDTO;
import com.sa.client_service.orders.application.dtos.CreateOrderItemDTO;
import com.sa.client_service.orders.application.dtos.MostPopularRestaurantDTO;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.domain.OrderItem;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.CreateOrderItemRequest;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.CreateOrderRequest;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.MostPopularRestaurantResponse;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.OrderItemResponse;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderRestMapper {
    @Mapping(source = "createOrderItemRequests", target = "createOrderItemDTOs")
    public CreateOrderDTO toDTO(CreateOrderRequest createOrderRequest);
    public CreateOrderItemDTO toDTO(CreateOrderItemRequest createOrderItemRequest);

    @Mapping(target = "clientCui", source = "client.cui")
    public OrderResponse toResponse(Order order);
    public List<OrderResponse> toResponse(List<Order> order);
    public OrderItemResponse toResponse(OrderItem orderItem);

    public MostPopularRestaurantResponse toResponse(MostPopularRestaurantDTO dto);
}
