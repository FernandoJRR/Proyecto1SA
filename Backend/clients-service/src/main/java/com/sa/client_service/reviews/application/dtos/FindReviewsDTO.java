package com.sa.client_service.reviews.application.dtos;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FindReviewsDTO {
    private String establishmentId;
    private String establishmentType;
    private String sourceId;
    private UUID clientId;
}
