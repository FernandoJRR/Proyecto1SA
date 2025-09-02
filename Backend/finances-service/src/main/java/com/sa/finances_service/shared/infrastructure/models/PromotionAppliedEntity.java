package com.sa.finances_service.shared.infrastructure.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionAppliedEntity {
    @Column(name = "promotion_id")
    private String promotionId;

    private String name;

    @Column(name = "amount_off")
    private BigDecimal amountOff;

    @Column(name = "percent_off")
    private BigDecimal percentOff;
}
