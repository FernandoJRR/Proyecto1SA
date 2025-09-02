package com.sa.client_service.shared.infrastructure.establishmentsadapter.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.outputports.FindRoomByHotelAndIdOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindRoomInHotelByIdAdapter implements FindRoomByHotelAndIdOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public Optional<RoomDTO> findByHotelAndId(String hotelId, String roomId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/{hotelId}/rooms/{roomId}";

        try {
            RoomDTO foundRoom = restClient.get()
                .uri(REQUEST_URL, hotelId,roomId)
                .retrieve()
                .toEntity(RoomDTO.class)
                .getBody();

            return Optional.of(foundRoom);
        } catch (HttpClientErrorException ex) {
            return Optional.empty();
        }
    }

}
