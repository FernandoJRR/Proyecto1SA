package com.sa.client_service.reservations.infrastructure.establishmentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.outputports.ReservationHydrationOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationHydrationAdapter implements ReservationHydrationOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public HotelDTO getHotel(String id) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/{hotelId}";

        try {
            HotelDTO foundHotel = restClient.get()
                .uri(REQUEST_URL, id)
                .retrieve()
                .body(HotelDTO.class);

            return foundHotel;
        } catch (HttpClientErrorException ex) {
            return null;
        }
    }

    @Override
    public RoomDTO getRoom(String hotelId, String roomId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/hotels/{hotelId}/rooms/{roomId}";

        try {
            RoomDTO foundHotel = restClient.get()
                .uri(REQUEST_URL, hotelId, roomId)
                .retrieve()
                .body(RoomDTO.class);

            return foundHotel;
        } catch (HttpClientErrorException ex) {
            return null;
        }
    }

}
