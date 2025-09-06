package com.sa.finances_service.promotions.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Promotion extends Auditor {
    private BigDecimal percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private UUID establishmentId;
    private EstablishmentTypeEnum establishmentType;
    private Integer topCount;
    private PromotionType promotionType;

    public Promotion(UUID id, BigDecimal percentage, LocalDate startDate, LocalDate endDate,
            String name, UUID establishmentId, EstablishmentTypeEnum establishmentType, Integer topCount, PromotionType promotionType) {
        super(id);
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionType = promotionType;
        this.name = name;
        this.establishmentId = establishmentId;
        this.establishmentType = establishmentType;
        this.topCount = topCount;
    }

    public static Promotion create(BigDecimal percentage, LocalDate startDate, LocalDate endDate,
            String name, UUID establishmentId, EstablishmentTypeEnum establishmentType, Integer topCount, PromotionType promotionType) {
        return new Promotion(UUID.randomUUID(), percentage, startDate, endDate,
        name, establishmentId, establishmentType, topCount, promotionType);
    }
}
