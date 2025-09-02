package com.sa.employee_service.employees.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.shared.dtos.IdRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateEmployeeDTO {
    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 100, message = "El primer nombre no puede tener más de 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    private String lastName;

    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El salario debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El salario debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal salary;

    @NotBlank(message = "El CUI es obligatorio")
    @Pattern(regexp = "^[0-9]{13}$", message = "El CUI debe ser un numero entero de 13 digitos")
    private String cui;

    @Valid
    @NotNull(message = "El employeeTypeId no puede ser nulo")
    private IdRequestDTO employeeTypeId;

    @Valid
    @NotNull(message = "El hiredAt no puede ser nulo")
    private LocalDate hiredAt;

    @Valid
    private UUID establishmentId;

    @Valid
    private CreateUserDTO createUserDTO;
}
