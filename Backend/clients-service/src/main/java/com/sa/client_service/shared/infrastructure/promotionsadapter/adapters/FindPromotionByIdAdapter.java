package com.sa.client_service.shared.infrastructure.promotionsadapter.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.PromotionDTO;
import com.sa.client_service.reservations.application.outputports.FindPromotionByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindPromotionByIdAdapter implements FindPromotionByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.clientsURL}")
    private String CLIENTS_SERVICE_URL;

    @Override
    public Optional<PromotionDTO> findPromotionById(String id) {
        final String REQUEST_URL = CLIENTS_SERVICE_URL+"/api/v1/promotions/{promotionId}";

        try {
            PromotionDTO foundPromotion = restClient.get()
                .uri(REQUEST_URL, id)
                .retrieve()
                .toEntity(PromotionDTO.class)
                .getBody();

            return Optional.of(foundPromotion);
        } catch (HttpClientErrorException ex) {
            return Optional.empty();
        }
    }

}
