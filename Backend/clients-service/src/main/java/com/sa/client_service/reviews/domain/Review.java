package com.sa.client_service.reviews.domain;

import java.util.UUID;

import com.sa.client_service.clients.domain.Client;
import com.sa.domain.Auditor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Review extends Auditor {
    private Client client;
    private String establishmentId;
    private EstablishmentType establishmentType;
    private String sourceId;
    private Rating rating;
    private String comment;

    public Review(UUID id,
    String establishmentId,
    EstablishmentType establishmentType,
    String sourceId, Rating rating, String comment) {
        super(id);
        this.establishmentId = establishmentId;
        this.establishmentType = establishmentType;
        this.sourceId = sourceId;
        this.rating = rating;
        this.comment = comment == null ? "" : comment.trim();
    }

    public static Review create(String establishmentId, EstablishmentType establishmentType, String sourceId, int rating, String comment) {
        return new Review(UUID.randomUUID(), establishmentId, establishmentType, sourceId, Rating.of(rating), comment);
    }
}
