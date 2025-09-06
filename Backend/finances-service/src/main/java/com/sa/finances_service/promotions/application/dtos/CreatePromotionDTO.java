package com.sa.finances_service.promotions.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromotionDTO {
    @NotNull(message = "El porcentaje es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El porcentaje debe ser mayor a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje debe ser maximo 100")
    private BigDecimal percentage;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    @NotBlank(message = "El tipo de promocion es obligatorio")
    private String promotionType;

    @NotBlank(message = "El ID del establecimiento es obligatorio")
    private String establishmentId;

    @NotBlank(message = "El tipo del establecimiento es obligatorio")
    private String establishmentType;

    @NotBlank(message = "El nombre de la promocion es obligatorio")
    private String name;

    @NotBlank(message = "El contador de top de la promocion es obligatorio")
    @Min(value = 1, message = "El contador puede tener como minimo el valor de 1")
    @Max(value = 10, message = "El contador puede tener como minimo el valor de 10")
    private Integer topCount;
}
