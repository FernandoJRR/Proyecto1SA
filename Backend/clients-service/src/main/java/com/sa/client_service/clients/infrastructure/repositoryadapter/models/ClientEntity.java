package com.sa.client_service.clients.infrastructure.repositoryadapter.models;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "client")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class ClientEntity extends AuditorEntity {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "cui", nullable = false, unique = true, length = 20)
    private String cui;

    @OneToMany(mappedBy = "client")
    private List<ReviewEntity> reviews;

    public ClientEntity(UUID id, String firstName, String lastName, String email, String cui) {
        super(id.toString());
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cui = cui;
    }
}
