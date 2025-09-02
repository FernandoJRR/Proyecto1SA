package com.sa.finances_service.payments.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.domain.Auditor;
import com.sa.finances_service.shared.domain.PromotionApplied;
import com.sa.finances_service.shared.domain.Promotionable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payment extends Auditor implements Promotionable {
    private UUID establishmentId;
    private UUID clientId;
    private SourceTypeEnum sourceType;
    private UUID sourceId;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private PaymentMethodEnum method;
    private PaymentStatusEnum status;

    private String cardNumber;

    private PromotionApplied promotionApplied;

    public Payment(
            UUID id,
            UUID establishmentId,
            UUID clientId,
            SourceTypeEnum sourceType,
            UUID sourceId,
            BigDecimal subtotal,
            BigDecimal discount,
            BigDecimal total,
            PaymentMethodEnum method,
            PaymentStatusEnum status,
            String cardNumber) {
        super(id);
        this.establishmentId = establishmentId;
        this.clientId = clientId;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
        this.method = method;
        this.status = status;
        this.cardNumber = cardNumber;
    }

    public static Payment create(
            UUID establishmentId,
            UUID clientId,
            SourceTypeEnum sourceType,
            UUID sourceId,
            BigDecimal subtotal,
            PaymentMethodEnum method,
            String cardNumber) {
        return new Payment(UUID.randomUUID(),
                establishmentId,
                clientId,
                sourceType,
                sourceId, subtotal, BigDecimal.valueOf(0), subtotal, method, PaymentStatusEnum.PAID, cardNumber);
    }

    @Override
    public void applyPromotion(String promotionId, String name, BigDecimal percentage) {
        BigDecimal amount = this.subtotal.multiply(percentage.divide(BigDecimal.valueOf(100)));
        PromotionApplied appliedPromotion = new PromotionApplied(UUID.fromString(promotionId), name, amount,
                percentage);
        this.total = this.total.subtract(amount);
        this.promotionApplied = appliedPromotion;
    }
}
