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
    private String hotelId;
    private String roomId;
    private Rating rating;
    private String comment;

    public Review(UUID id, String hotelId, String roomId, Rating rating, String comment) {
        super(id);
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.rating = rating;
        this.comment = comment == null ? "" : comment.trim();
    }

    public static Review create(String hotelId, String roomId, int rating, String comment) {
        return new Review(UUID.randomUUID(), hotelId, roomId, Rating.of(rating), comment);
    }
}
