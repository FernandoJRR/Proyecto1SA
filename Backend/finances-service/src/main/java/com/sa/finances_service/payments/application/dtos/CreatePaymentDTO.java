package com.sa.finances_service.payments.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentDTO {
    @NotNull(message = "El ID del establecimiento es obligatorio.")
    private UUID establishmentId;

    @NotNull(message = "El ID del cliente es obligatorio.")
    private UUID clientId;

    @NotNull(message = "El tipo de origen del pago es obligatorio.")
    @Pattern(regexp = "ORDER|RESERVATION", message = "El tipo de origen de pago ingresado no existe.")
    private String sourceType;

    @NotNull(message = "El ID del origen del pago es obligatorio")
    private UUID sourceId;

    @NotNull(message = "El metodo de pago es obligatorio")
    @Pattern(regexp = "CASH|CARD", message = "El metodo de pago ingresado no existe.")
    private String method;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El subtotal debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El subtotal debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal subtotal;

    @Size(min = 4, max = 19, message = "El numero de tarjeta debe tener entre 4 y 19 caracteres.")
    @Pattern(regexp = "\\d", message = "El numero de tarjeta debe de ser un numero.")
    private String cardNumber;

    private String promotionId;
}
