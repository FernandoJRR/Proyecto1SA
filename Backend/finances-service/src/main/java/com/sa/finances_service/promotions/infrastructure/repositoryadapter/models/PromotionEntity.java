package com.sa.finances_service.promotions.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PromotionEntity extends AuditorEntity {
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PromotionType promotionType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String establishmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstablishmentTypeEnum establishmentType;

    @Column(nullable = false)
    private Integer topCount;
}
