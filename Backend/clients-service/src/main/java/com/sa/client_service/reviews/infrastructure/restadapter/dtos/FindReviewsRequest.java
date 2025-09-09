package com.sa.client_service.reviews.infrastructure.restadapter.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FindReviewsRequest {
    private String establishmentId;
    private String establishmentType;
    private String sourceId;
    private UUID clientId;
}
