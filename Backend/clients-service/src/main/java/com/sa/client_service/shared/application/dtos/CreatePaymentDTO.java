package com.sa.client_service.shared.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePaymentDTO {
    private UUID establishmentId;

    private UUID clientId;

    private String sourceType;

    private UUID sourceId;

    private String method;

    private BigDecimal subtotal;

    private String cardNumber;

    private String promotionId;

    private LocalDate paidAt;
}
