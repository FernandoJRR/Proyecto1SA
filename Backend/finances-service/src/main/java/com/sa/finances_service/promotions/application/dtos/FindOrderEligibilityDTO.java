package com.sa.finances_service.promotions.application.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindOrderEligibilityDTO {

    @NotBlank(message = "El ID del cliente es obligatorio.")
    private String clientId;

    @NotBlank(message = "El ID del restaurante es obligatorio.")
    private String restaurantId;

    @NotEmpty(message = "Es obligatorio al menos un item en la orden.")
    private List<String> dishesIds;

    @NotNull(message = "La fecha de la orden es obligatoria.")
    private LocalDate orderedAt;
}
