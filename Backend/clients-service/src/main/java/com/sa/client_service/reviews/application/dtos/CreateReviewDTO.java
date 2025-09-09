package com.sa.client_service.reviews.application.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateReviewDTO {
    @NotNull(message = "El CUI del cliente es obligatorio")
    private String clientCui;

    @NotBlank(message = "El ID del establecimiento es obligatorio")
    private String establishmentId;

    @NotBlank(message = "El tipo de establecimiento es obligatorio")
    private String establishmentType;

    @NotBlank(message = "El ID del origen es obligatorio")
    private String sourceId;

    @Min(value = 1, message = "El rating debe ser de al menos 1")
    @Max(value = 5, message = "El rating debe de ser de maximo 5")
    private int rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;
}
