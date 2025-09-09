package com.sa.establishment_service.shared.infrastructure.utils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor((request, body, execution) -> {
                    String jwt = JwtFilter.getCurrentToken();
                    if (jwt != null) {
                        request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                    }
                    return execution.execute(request, body);
                })
                .baseUrl("")
                .build();
    }
}
