package com.sa.infrastructure.jwtadapter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ServiceTokenProvider {

    @Value("${app.serviceToken:}")
    private String serviceToken;

    public ServiceTokenProvider() {
    }

    public Optional<String> getToken() {
        return Optional.ofNullable(serviceToken)
                       .filter(s -> !s.isBlank());
    }
}