package com.sa.client_service.reviews.infrastructure.repositoryadapter.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends AuditorEntity {

    @Column(nullable = false)
    private String hotelId;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false, length = 2000)
    private String comment;

    private Integer rating;

    @ManyToOne
    private ClientEntity client;
}
