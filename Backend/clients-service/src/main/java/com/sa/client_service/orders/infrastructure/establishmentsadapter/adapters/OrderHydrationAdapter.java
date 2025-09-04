package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.orders.application.dtos.RestaurantDTO;
import com.sa.client_service.orders.application.outputports.OrderHydrationOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderHydrationAdapter implements OrderHydrationOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public RestaurantDTO getRestaurant(String restaurantId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/restaurants/{restaurantId}";

        try {
            RestaurantDTO restaurant = restClient.get()
                .uri(REQUEST_URL, restaurantId)
                .retrieve()
                .body(RestaurantDTO.class);

            return restaurant;
        } catch (HttpClientErrorException ex) {
            return null;
        }
    }

}
