package com.sa.client_service.reviews.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReviewRequest {

    private String clientCui;

    private String establishmentId;

    private String establishmentType;

    private String sourceId;

    private int rating;

    private String comment;
}
