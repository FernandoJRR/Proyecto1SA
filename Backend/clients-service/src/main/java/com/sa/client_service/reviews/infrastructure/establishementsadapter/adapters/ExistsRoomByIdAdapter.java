package com.sa.client_service.reviews.infrastructure.establishementsadapter.adapters;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.sa.client_service.reviews.application.outputports.ExistsRoomByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsRoomByIdAdapter implements ExistsRoomByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;


    @Override
    public boolean existsById(String roomId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/rooms/{roomId}/exists";

        try {
            restClient.get()
                .uri(REQUEST_URL, roomId)
                .retrieve()
                .toBodilessEntity();

            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }

}
