package com.sa.finances_service.payments.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.PaymentStatusEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.shared.infrastructure.models.PromotionAppliedEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PaymentEntity extends AuditorEntity {
    @Column(name = "establishment_id", nullable = false)
    private String establishmentId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 16)
    private SourceTypeEnum sourceType;

    @Column(name = "source_id", nullable = false)
    private String sourceId;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotal;

    @Column(name = "discount", nullable = false, precision = 19, scale = 4)
    private BigDecimal discount;

    @Column(name = "total", nullable = false, precision = 19, scale = 4)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 16)
    private PaymentMethodEnum method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private PaymentStatusEnum status;

    @Column(name = "card_number", length = 19)
    private String cardNumber;

    @Embedded
    private PromotionAppliedEntity promotionApplied;

    @Column(name = "paid_at", length = 19)
    private LocalDate paidAt;
}
