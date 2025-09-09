package com.sa.establishment_service.shared.infrastructure.financesadapter.adapters;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.sa.establishment_service.shared.application.dtos.PaymentDTO;
import com.sa.establishment_service.shared.application.outputports.FindPaymentsByEstablishmentOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindPaymentsByEstablishmentAdapter implements FindPaymentsByEstablishmentOutputPort {

    private final RestClient restClient;

    @Value("${app.financesURL}")
    private String FINANCES_SERVICE_URL;

    @Override
    public List<PaymentDTO> findByEstablishment(String id, LocalDate fromDate, LocalDate toDate) {
        //final String REQUEST_URL = FINANCES_SERVICE_URL+"/api/v1/payments";
        String url = UriComponentsBuilder
                .fromUriString(FINANCES_SERVICE_URL)
                .path("/api/v1/payments")
                .queryParam("establishmentId", id)
                .queryParamIfPresent("fromDate", Optional.ofNullable(fromDate))
                .queryParamIfPresent("toDate", Optional.ofNullable(toDate))
                .build()
                .toUriString();

        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<PaymentDTO>>() {});
        } catch (HttpClientErrorException ex) {
            System.out.println(ex);
            return List.of();
        }
    }

}
