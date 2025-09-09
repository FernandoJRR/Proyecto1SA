package com.sa.establishment_service.shared.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentResponse {
    private UUID establishmentId;
    private UUID clientId;
    private String sourceType;
    private UUID sourceId;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private LocalDate paidAt;

    private String description;
}
