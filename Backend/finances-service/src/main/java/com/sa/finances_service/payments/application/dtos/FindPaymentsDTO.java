package com.sa.finances_service.payments.application.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindPaymentsDTO {

    private String establishmentId;

    private String clientId;

    @Pattern(regexp = "ORDER|RESERVATION", message = "Invalid sourceType")
    private String sourceType;

    private String sourceId;

    @Pattern(regexp = "CASH|CARD|TRANSFER", message = "Invalid method")
    private String method;

    private String promotionId;

    private LocalDate fromDate;
    private LocalDate toDate;
}