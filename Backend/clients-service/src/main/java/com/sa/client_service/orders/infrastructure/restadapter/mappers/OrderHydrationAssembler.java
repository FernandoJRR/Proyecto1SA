package com.sa.client_service.orders.infrastructure.restadapter.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sa.client_service.clients.infrastructure.restadapter.mappers.ClientRestMapper;
import com.sa.client_service.orders.application.outputports.OrderHydrationOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.OrderHydratedResponse;
import com.sa.client_service.shared.infrastructure.restadapter.mappers.PromotionRestMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderHydrationAssembler {

    private final OrderHydrationOutputPort orderHydrationOutputPort;
    private final PromotionRestMapper promotionRestMapper;
    private final ClientRestMapper clientRestMapper;
    private final OrderRestMapper orderRestMapper;

    public OrderHydratedResponse toResponse(Order r) {
        var restaurant = orderHydrationOutputPort.getRestaurant(r.getRestaurantId().toString());
        return new OrderHydratedResponse(
                r.getId().toString(),
                r.getClient().getCui(),
                clientRestMapper.toResponse(r.getClient()),
                r.getRestaurantId(),
                restaurant,
                r.getItems().stream().map(orderRestMapper::toResponse).toList(),
                r.getTotal(),
                r.getSubtotal(),
                promotionRestMapper.toResponse(r.getPromotionApplied()));
    }

    public List<OrderHydratedResponse> toResponseList(List<Order> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
