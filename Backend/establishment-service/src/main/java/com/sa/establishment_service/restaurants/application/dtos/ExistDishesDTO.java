package com.sa.establishment_service.restaurants.application.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExistDishesDTO {
    @NotNull(message = "El ID del restaurante es obligatorio para hacer la busqueda.")
    private UUID restaurantId;
    @NotEmpty(message = "Debe de ingresar al menos un ID de platillo.")
    private List<UUID> dishIds;
}
