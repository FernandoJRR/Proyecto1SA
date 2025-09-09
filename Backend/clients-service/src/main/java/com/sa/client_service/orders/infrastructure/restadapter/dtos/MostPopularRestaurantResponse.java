package com.sa.client_service.orders.infrastructure.restadapter.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MostPopularRestaurantResponse {
    private String restaurantName;
    private List<OrderResponse> orders;
}
