package com.sa.employee_service.employees.infrastructure.repositoryadapter.models;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "employeeHistory")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class EmployeeHistory extends AuditorEntity {
    @ManyToOne
    private HistoryType historyType;

    @ManyToOne
    private EmployeeEntity employee;

    @Column(length = 200)
    private String commentary;

    @Column(nullable = true)
    private LocalDate historyDate;

    public EmployeeHistory(String commentary) {
        this.commentary = commentary;
    }
}
