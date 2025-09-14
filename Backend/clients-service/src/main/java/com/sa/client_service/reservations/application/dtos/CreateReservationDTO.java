package com.sa.client_service.reservations.application.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDTO {

    @NotNull(message = "El CUI del cliente es obligatorio")
    private String clientCui;

    @NotNull(message = "El ID del hotel es obligatorio")
    private UUID hotelId;

    @NotNull(message = "El ID de la habitacion es obligatorio")
    private UUID roomId;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate endDate;

    private UUID promotionId;
}
