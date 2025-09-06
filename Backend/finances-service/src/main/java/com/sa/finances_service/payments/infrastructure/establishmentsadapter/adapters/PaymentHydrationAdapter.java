package com.sa.finances_service.payments.infrastructure.establishmentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.finances_service.payments.application.dtos.HotelDTO;
import com.sa.finances_service.payments.application.dtos.RestaurantDTO;
import com.sa.finances_service.payments.application.outputports.PaymentHydrationOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentHydrationAdapter implements PaymentHydrationOutputPort {

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
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public RestaurantDTO getRestaurant(String restaurantId) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/restaurants/{restaurantId}";

        try {
            RestaurantDTO foundRestaurant = restClient.get()
                .uri(REQUEST_URL, restaurantId)
                .retrieve()
                .body(RestaurantDTO.class);

            return foundRestaurant;
        } catch (Exception ex) {
            return null;
        }
    }

}
