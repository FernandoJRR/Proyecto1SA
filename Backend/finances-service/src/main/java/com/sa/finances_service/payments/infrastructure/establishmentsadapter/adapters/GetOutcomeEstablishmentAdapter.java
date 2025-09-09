package com.sa.finances_service.payments.infrastructure.establishmentsadapter.adapters;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.sa.finances_service.payments.application.outputports.GetOutcomeByDateOutputPort;
import com.sa.finances_service.reports.application.dtos.OutcomeDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetOutcomeEstablishmentAdapter implements GetOutcomeByDateOutputPort {

    private final RestClient restClient;

    @Value("${app.establishmentsURL}")
    private String ESTABLISHMENT_SERVICE_URL;

    @Override
    public Optional<OutcomeDTO> getByDate(LocalDate fromDate, LocalDate toDate) {
        final String REQUEST_URL = ESTABLISHMENT_SERVICE_URL+"/api/v1/establishments/reports/outcome";

        try {
            OutcomeDTO foundOutcome = restClient.get()
                .uri(UriComponentsBuilder.fromUriString(REQUEST_URL)
                    .queryParam("fromDate", fromDate)
                    .queryParam("toDate", toDate)
                    .toUriString())
                .retrieve()
                .body(OutcomeDTO.class);

            return Optional.of(foundOutcome);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

}
