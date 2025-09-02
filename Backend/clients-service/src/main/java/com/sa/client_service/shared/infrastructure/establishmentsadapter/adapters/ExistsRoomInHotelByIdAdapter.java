package com.sa.client_service.shared.infrastructure.establishmentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.shared.application.outputports.ExistsRoomInHotelByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsRoomInHotelByIdAdapter implements ExistsRoomInHotelByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public boolean existsById(String hotelId, String roomId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/{hotelId}/rooms/{roomId}/exists";

        try {
            restClient.get()
                .uri(REQUEST_URL, hotelId,roomId)
                .retrieve()
                .toBodilessEntity();

            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }

}
