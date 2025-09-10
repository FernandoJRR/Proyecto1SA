package com.sa.client_service.reservations.infrastructure.establishmentsadapter.adapters;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.outputports.FindAllRoomsOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindAllRoomsAdapter implements FindAllRoomsOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public List<RoomDTO> findAll() {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/rooms";

        try {
            List<RoomDTO> rooms = restClient.get()
                .uri(REQUEST_URL)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RoomDTO>>() {});

            return rooms;
        } catch (HttpClientErrorException ex) {
            return List.of();
        }
    }

}
