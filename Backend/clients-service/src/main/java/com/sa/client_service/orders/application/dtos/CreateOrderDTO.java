package com.sa.client_service.orders.application.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateOrderDTO {
    @NotBlank(message = "El CUI del cliente es obligatorio")
    private String clientCui;

    @NotNull(message = "El ID del restaurante es obligatorio")
    private UUID restaurantId;

    private UUID promotionId;

    @Valid
    @NotEmpty(message = "Debe haber al menos un item en la orden")
    private List<CreateOrderItemDTO> createOrderItemDTOs;
}
