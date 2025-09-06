package com.sa.finances_service.promotions.infrastructure.clientsadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.sa.finances_service.payments.application.dtos.RoomDTO;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;
import com.sa.finances_service.promotions.application.outputports.MostPopularRoomsOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MostPopularRoomsAdapter implements MostPopularRoomsOutputPort {

    private final RestClient restClient;

    @Value("${app.clientsURL}")
    private String CLIENT_SERVICE_URL;

    @Override
    public List<UUID> findMostPopular(String hotelId, Integer limit) {
        final String REQUEST_URL = CLIENT_SERVICE_URL+"/api/v1/reservations/by-hotel/{hotelId}/most-popular-rooms";

        try {
            List<UUID> rooms  = restClient.get()
                .uri(REQUEST_URL, hotelId)
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
