package com.sa.employee_service.employees.infrastructure.establishmentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.employee_service.employees.application.outputports.ExistsHotelByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsHotelByIdAdapter implements ExistsHotelByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public boolean existsById(String hotelId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/{hotelId}/exists";

        try {
            restClient.get()
                .uri(REQUEST_URL, hotelId)
                .retrieve()
                .toBodilessEntity();

            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }

}
