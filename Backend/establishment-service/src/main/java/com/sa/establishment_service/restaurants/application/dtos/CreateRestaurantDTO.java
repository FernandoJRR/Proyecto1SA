package com.sa.establishment_service.restaurants.application.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRestaurantDTO {

    @Valid
    @NotBlank(message = "El nombre del restaurante es obligatorio")
    public String name;

    @Valid
    @NotBlank(message = "La direccion el restaurante es obligatoria")
    public String address;

    @Valid
    public String hotelId;

}
