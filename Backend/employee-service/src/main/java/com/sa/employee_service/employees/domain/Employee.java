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

    public String getFullName() {
        String fullname = String.format("%s %s", firstName, lastName);
        return fullname;
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

    /*
    public void attachUser(User user) {
        this.user = user;
        if (user != null && user.getEmployee() != this) {
            user.setEmployee(this);
        }
    }
    */

    public void changeSalary(java.math.BigDecimal newSalary) {
        this.salary = requirePositive(newSalary, "newSalary");
    }

    public void deactivate(java.time.LocalDate date) {
        if (this.desactivatedAt != null) {
            throw new IllegalStateException("Employee already deactivated");
        }
        this.desactivatedAt = date != null ? date : java.time.LocalDate.now();
    }

    public void reactivate() {
        if (this.desactivatedAt == null) {
            throw new IllegalStateException("Employee already active");
        }
        this.desactivatedAt = null;
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    private static java.math.BigDecimal requirePositive(java.math.BigDecimal value, String field) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalArgumentException(field + " must be > 0");
        }
        return value;
    }

    private static java.math.BigDecimal requirePercentOrNull(java.math.BigDecimal value, String field) {
        if (value == null) return null;
        if (value.compareTo(java.math.BigDecimal.ZERO) < 0 || value.compareTo(new java.math.BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(field + " must be within [0, 100]");
        }
        return value;
    }
}
