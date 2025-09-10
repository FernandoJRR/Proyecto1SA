package com.sa.client_service.shared.infrastructure;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.sa.infrastructure.jwtadapter.ServiceTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;

@Configuration
public class RestClientConfig {

    private final ServiceTokenProvider serviceTokenProvider;

    public RestClientConfig(ServiceTokenProvider serviceTokenProvider) {
        this.serviceTokenProvider = serviceTokenProvider;
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor((request, body, execution) -> {
                    String jwt = serviceTokenProvider.getToken()
                        .orElse(JwtFilter.getCurrentToken());

                    if (jwt != null) {
                        request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                    }
                    return execution.execute(request, body);
                })
                .baseUrl("")
                .build();
    }
}
