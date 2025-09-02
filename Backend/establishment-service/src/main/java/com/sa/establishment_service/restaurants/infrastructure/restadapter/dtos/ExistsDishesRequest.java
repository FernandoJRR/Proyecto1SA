package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

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
public class ExistsDishesRequest {

    @NotEmpty(message = "Debes ingresar al menos un platillo para verificarlo")
    private List<@NotNull UUID> dishIds;
}
