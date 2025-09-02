package com.sa.employee_service.employees.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@NoArgsConstructor
@DynamicUpdate
@Getter
@Setter
public class EmployeeEntity extends AuditorEntity {

    @Column(length = 100, unique = true, nullable = false)
    private String cui;
    @Column(length = 100)
    private String firstName;
    @Column(length = 100)
    private String lastName;
    @Column(scale = 2)
    private BigDecimal salary;
    @Column(nullable = true)
    private LocalDate desactivatedAt;
    @Column(nullable = false)
    private LocalDate hiredAt;

    @Column(nullable = true, name = "establishment_id")
    private UUID establishmentId;

    @ManyToOne
    private EmployeeTypeEntity employeeType;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private UserEntity user;

    /**
     * Para la creacion de nuevos empleados
     *
     * @param firstName
     * @param lastName
     * @param salary
     * @param igssPercentage
     * @param irtraPercentage
     */
    public EmployeeEntity(String cui, String firstName, String lastName, BigDecimal salary) {
        super();
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public EmployeeEntity(String cui, String firstName, String lastName, BigDecimal salary,
            LocalDateTime resignDate, EmployeeTypeEntity employeeType, UserEntity user) {
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.employeeType = employeeType;
        this.user = user;
    }

    public EmployeeEntity(String id) {
        super(id);
    }

    public String getFullName() {
        String fullname = String.format("%s %s", firstName, lastName);
        return fullname;
    }
}
