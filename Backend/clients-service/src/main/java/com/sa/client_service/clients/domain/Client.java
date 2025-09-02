package com.sa.client_service.clients.domain;

import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Client extends Auditor {
    private String firstName;
    private String lastName;
    private String email;
    private String cui;

    public Client(UUID id, String firstName, String lastName, String email, String cui) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cui = cui;
    }

    public static Client register(String firstName, String lastName, String email, String cui) {
        return new Client(UUID.randomUUID(), firstName, lastName, email, cui);
    }
}
