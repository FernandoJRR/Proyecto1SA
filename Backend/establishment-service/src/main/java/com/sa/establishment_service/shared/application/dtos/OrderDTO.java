package com.sa.establishment_service.shared.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDTO {
    private String id;
    private String clientCui;
    private UUID restaurantId;
    private BigDecimal total;
    private BigDecimal subtotal;
}
