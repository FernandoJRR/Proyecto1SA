package com.sa.finances_service.promotions.application.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FindReservationEligibilityDTO {
    @NotBlank(message = "El ID del cliente es obligatorio.")
    private String clientId;

    @NotBlank(message = "El ID del hotel es obligatorio.")
    private String hotelId;

    @NotBlank(message = "El ID de la habitacion es obligatorio.")
    private String roomId;

    @NotNull(message = "La fecha de inicio de la reservacion es obligatoria.")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin de la reservacion es obligatoria.")
    private LocalDate endDate;
}
