package com.sa.finances_service.promotions.infrastructure.clientsadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.sa.finances_service.promotions.application.outputports.MostPopularDishesOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MostPopularDishesAdapter implements MostPopularDishesOutputPort {

    private final RestClient restClient;

    @Value("${app.clientsURL}")
    private String CLIENT_SERVICE_URL;

    @Override
    public List<UUID> findMostPopular(String restaurantId, Integer limit) {
        final String REQUEST_URL = CLIENT_SERVICE_URL+"/api/v1/orders/by-restaurant/{restaurantId}/most-popular-dishes";

        try {
            List<UUID> rooms  = restClient.get()
                .uri(REQUEST_URL, restaurantId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<UUID>>() {});

            if (rooms == null) {
                return List.of();
            }

            return rooms.stream().limit(limit).toList();
        } catch (Exception ex) {
            return List.of();
        }
    }
}
