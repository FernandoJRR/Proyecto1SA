package com.sa.employee_service.employees.infrastructure.repositoryadapter.models;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "historyType")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class HistoryType extends AuditorEntity {
    @Column(length = 100)
    private String type;

    public HistoryType(String type) {
        this.type = type;
    }
}
