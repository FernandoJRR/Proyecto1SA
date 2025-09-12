package com.sa.employee_service.employees.application.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateEmployeeDTO {
    @NotBlank
    @Size(max = 100, message = "El numero de caracteres maximo para el nombre es 100")
    private String firstName;

    @NotBlank
    @Size(max = 100, message = "El numero de caracteres maximo para el apellido es 100")
    private String lastName;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Min(value = 0, message = "El salario debe ser mayor a 0")
    private BigDecimal salary;
}
