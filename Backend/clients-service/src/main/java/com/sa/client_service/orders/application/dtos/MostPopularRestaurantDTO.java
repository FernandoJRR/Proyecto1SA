package com.sa.client_service.orders.application.dtos;

import java.util.List;

import com.sa.client_service.orders.domain.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MostPopularRestaurantDTO {
    private String restaurantName;
    private List<Order> orders;
}
