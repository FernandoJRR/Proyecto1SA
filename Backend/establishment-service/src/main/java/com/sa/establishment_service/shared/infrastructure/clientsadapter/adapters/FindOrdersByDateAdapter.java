package com.sa.establishment_service.shared.infrastructure.clientsadapter.adapters;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.sa.establishment_service.shared.application.dtos.OrderDTO;
import com.sa.establishment_service.shared.application.outputports.FindOrdersByDateOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindOrdersByDateAdapter implements FindOrdersByDateOutputPort {

    private final RestClient restClient;

    @Value("${app.clientsURL}")
    private String CLIENTS_SERVICE_URL;

    @Override
    public List<OrderDTO> findOrdersByDate(LocalDate fromDate, LocalDate toDate) {
        //final String REQUEST_URL = FINANCES_SERVICE_URL+"/api/v1/payments";
        String url = UriComponentsBuilder
                .fromUriString(CLIENTS_SERVICE_URL)
                .path("/api/v1/orders")
                .queryParamIfPresent("fromDate", Optional.ofNullable(fromDate))
                .queryParamIfPresent("toDate", Optional.ofNullable(toDate))
                .build()
                .toUriString();

        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<OrderDTO>>() {});
        } catch (HttpClientErrorException ex) {
            System.out.println(ex);
            return List.of();
        }
    }

}
