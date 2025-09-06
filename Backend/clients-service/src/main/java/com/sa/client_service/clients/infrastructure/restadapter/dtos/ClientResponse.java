package com.sa.client_service.clients.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String cui;
}
