package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.orders.application.outputports.ExistsRestaurantByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsRestaurantByIdAdapter implements ExistsRestaurantByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public boolean existsById(String restaurantId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/restaurants/{restaurantId}/exists";

        try {
            restClient.get()
                .uri(REQUEST_URL, restaurantId)
                .retrieve()
                .toBodilessEntity();

            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }

}
