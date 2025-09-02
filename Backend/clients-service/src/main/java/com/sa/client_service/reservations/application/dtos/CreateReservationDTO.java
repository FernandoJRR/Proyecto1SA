package com.sa.client_service.reservations.application.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;

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
    @FutureOrPresent(message = "La fecha de ingreso debe de ser una fecha futura")
    private LocalDate startDate;

    @NotNull(message = "La fecha de salida es obligatoria")
    @FutureOrPresent(message = "La fecha de salida debe de ser una fecha futura")
    private LocalDate endDate;

    private UUID promotionId;
}
