package com.sa.client_service.reservations.infrastructure.establishmentsadapter.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.outputports.FindHotelByIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindHotelByIdAdapter implements FindHotelByIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public Optional<HotelDTO> findById(String hotelId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/{hotelId}";

        try {
            HotelDTO foundHotel = restClient.get()
                .uri(REQUEST_URL, hotelId)
                .retrieve()
                .body(HotelDTO.class);

            return Optional.of(foundHotel);
        } catch (HttpClientErrorException ex) {
            return Optional.empty();
        }
    }

}
