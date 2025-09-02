package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO;
import com.sa.client_service.orders.application.outputports.ExistDishesByRestaurantOutputPort;
import com.sa.client_service.orders.infrastructure.establishmentsadapter.dtos.ExistantDishesRestaurantRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistantDishesRestaurantAdapter implements ExistDishesByRestaurantOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public ExistantDishesRestaurantDTO existantDishesRestaurant(String restaurantId, List<UUID> dishesId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/restaurants/{restaurantId}/dishes/query";

        return restClient.post()
        .uri(REQUEST_URL, restaurantId)
        .body(new ExistantDishesRestaurantRequest(dishesId))
        .retrieve()
        .toEntity(ExistantDishesRestaurantDTO.class)
        .getBody();
    }

}
