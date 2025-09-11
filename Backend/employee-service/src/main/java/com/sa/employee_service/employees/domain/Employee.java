package com.sa.employee_service.employees.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@NoArgsConstructor
@Getter
@Setter
public class Employee extends Auditor {
    private String cui;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private LocalDate desactivatedAt;
    private LocalDate hiredAt;

    private EmployeeType employeeType;

    private UUID establishmentId;
    private EstablishmentTypeEnum establishmentType;

    /**
     * Para la creacion de nuevos empleados
     *
     * @param firstName
     * @param lastName
     * @param salary
     */
    public Employee(String cui, String firstName, String lastName, BigDecimal salary) {
        super();
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public Employee(UUID id, String cui, String firstName, String lastName, BigDecimal salary,
            EmployeeType employeeType, LocalDate hiredAt, UUID establishmentId, EstablishmentTypeEnum establishmentType) {
        super(id);
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.employeeType = employeeType;
        this.hiredAt = hiredAt;
        this.establishmentId = establishmentId;
        this.establishmentType = establishmentType;
    }

    public Employee(String cui, String firstName, String lastName, BigDecimal salary,
            EmployeeType employeeType, LocalDate hiredAt) {
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.employeeType = employeeType;
        this.hiredAt = hiredAt;
    }

    public Employee(UUID id) {
        super(id);
    }

    public static Employee hire(
            String cui,
            String firstName,
            String lastName,
            java.math.BigDecimal initialSalary,
            EmployeeType type,
            LocalDate hiredAt,
            UUID establishmentId,
            String establishmentType
    ) {
        EstablishmentTypeEnum chosenEstablishmentType = null;
        if (establishmentId != null) {
            chosenEstablishmentType = EstablishmentTypeEnum.valueOf(establishmentType);
        }
        Employee e = new Employee(
            UUID.randomUUID(),
            cui,
            firstName,
            lastName,
            initialSalary,
            type,
            hiredAt,
            establishmentId,
            chosenEstablishmentType
        );
        return e;
    }
}
