package com.sa.establishment_service.restaurants.application.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRestaurantDTO {

    @Valid
    @NotBlank(message = "El nombre del restaurante no puede estar en blanco")
    private String name;

    @Valid
    @NotBlank(message = "La direccion del restaurante no puede estar en blanco")
    private String address;
}

