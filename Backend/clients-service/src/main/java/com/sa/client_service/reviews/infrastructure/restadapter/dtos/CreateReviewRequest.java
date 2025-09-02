package com.sa.client_service.reviews.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReviewRequest {

    private String clientCui;

    private String hotelId;

    private String roomId;

    private int rating;

    private String comment;
}
