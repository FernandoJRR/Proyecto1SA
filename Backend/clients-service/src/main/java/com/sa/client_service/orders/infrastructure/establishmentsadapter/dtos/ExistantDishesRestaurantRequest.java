package com.sa.client_service.orders.infrastructure.establishmentsadapter.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExistantDishesRestaurantRequest {
    private List<UUID> dishIds;
}
