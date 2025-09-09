package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.orders.application.dtos.RestaurantDTO;
import com.sa.client_service.orders.application.outputports.FindRestaurantByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindRestaurantByIdAdapter implements FindRestaurantByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public Optional<RestaurantDTO> findById(String restaurantId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/restaurants/{restaurantId}";

        try {
            RestaurantDTO restaurant = restClient.get()
                .uri(REQUEST_URL, restaurantId)
                .retrieve()
                .body(RestaurantDTO.class);

            return Optional.of(restaurant);
        } catch (HttpClientErrorException ex) {
            return Optional.empty();
        }
    }

}
