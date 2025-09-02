package com.sa.employee_service.employees.infrastructure.repositoryadapter.models;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "paymentType")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class PaymentType extends AuditorEntity {
    @Column(length = 100)
    private String type;

    public PaymentType(String type) {
        this.type = type;
    }
}
