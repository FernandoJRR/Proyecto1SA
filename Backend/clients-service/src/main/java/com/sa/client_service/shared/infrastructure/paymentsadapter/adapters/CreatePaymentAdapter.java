package com.sa.client_service.shared.infrastructure.paymentsadapter.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.shared.application.dtos.CreatePaymentDTO;
import com.sa.client_service.shared.application.outputports.CreatePaymentOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreatePaymentAdapter implements CreatePaymentOutputPort {

    private final RestClient restClient;

    @Value("${app.financesURL}")
    private String FINANCES_SERVICE_URL;

    @Override
    public boolean createPayment(CreatePaymentDTO createPaymentDTO) {
        final String REQUEST_URL = FINANCES_SERVICE_URL+"/api/v1/payments";
        System.out.println("NUMEROROOOO");
        System.out.println(createPaymentDTO.getCardNumber());

        try {
            restClient.post()
                .uri(REQUEST_URL)
                .body(createPaymentDTO)
                .retrieve()
                .toBodilessEntity();

            return true;
        } catch (HttpClientErrorException ex) {
            System.out.println(ex);
            return false;
        }
    }

}
