package com.sa.employee_service.employees.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.employee_service.users.infrastructure.restadapter.dtos.CreateUserRequest;
import com.sa.shared.dtos.IdRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateEmployeeRequest extends EmployeeRequestDTO {

    private String firstName;

    private String lastName;

    private BigDecimal salary;

    private String cui;

    private String establishmentType;

    private IdRequestDTO employeeTypeId;

    private LocalDate hiredAt;

    private UUID establishmentId;

    private CreateUserRequest createUserRequest;
}
