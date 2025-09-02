package com.sa.establishment_service.hotels.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomDTO {
    @NotBlank(message = "El numero de habitacion no puede ser vacio")
    @Size(max = 30, message = "El numero de habitacion no puede exceder los 30 caracteres")
    private String number;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal pricePerNight;

    @NotNull(message = "La capacidad de la habitacion es obligatoria")
    @Min(value = 1, message = "La capacidad minima de una habitacion es de 1 persona")
    private Integer capacity;

    @NotNull(message = "El ID del hotel es obligatorio")
    private UUID hotelId;
}
