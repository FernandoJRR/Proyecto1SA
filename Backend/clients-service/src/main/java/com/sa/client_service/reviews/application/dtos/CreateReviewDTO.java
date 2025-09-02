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

    @NotBlank(message = "El ID del hotel es obligatorio")
    private String hotelId;

    @NotBlank(message = "El ID de la habitacion es obligatorio")
    private String roomId;

    @Min(value = 1, message = "El rating debe ser de al menos 1")
    @Max(value = 5, message = "El rating debe de ser de maximo 5")
    private int rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;
}
