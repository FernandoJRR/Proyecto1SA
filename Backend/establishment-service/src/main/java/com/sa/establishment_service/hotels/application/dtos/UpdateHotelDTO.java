package com.sa.establishment_service.hotels.application.dtos;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateHotelDTO {

    @Valid
    @NotBlank(message = "El nombre del hotel no puede estar en blanco")
    private String name;

    @Valid
    @NotBlank(message = "La direccion del hotel no puede estar en blanco")
    private String address;

    @Valid
    @NotNull(message = "El costo de mantenimiento semanal es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El costo de mantenimiento debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El costo de mantenimiento debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal maintenanceCostPerWeek;
}
