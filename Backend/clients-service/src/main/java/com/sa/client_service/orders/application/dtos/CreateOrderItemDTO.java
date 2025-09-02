package com.sa.client_service.orders.application.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrderItemDTO {
    @NotBlank(message = "El ID del platillo es obligatorio")
    private UUID dishId;

    @NotNull(message = "La cantidad de consumo es obligatoria")
    @Min(value = 1, message = "La cantidad minima de consumo es 1")
    @Max(value = 10, message = "La cantidad maxima de consumo es 10")
    private Integer quantity;
}
