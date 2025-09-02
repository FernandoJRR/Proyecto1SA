package com.sa.client_service.orders.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.shared.domain.PromotionApplied;
import com.sa.client_service.shared.domain.Promotionable;
import com.sa.domain.Auditor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends Auditor implements Promotionable {
    private Client client;
    private UUID restaurantId;
    private List<OrderItem> items;

    private BigDecimal total;
    private BigDecimal subtotal;

    private PromotionApplied promotionApplied;

    public Order(UUID id,
            Client client,
            UUID restaurantId,
            List<OrderItem> items,
            BigDecimal total) {

        super(id);
        this.client = client;
        this.restaurantId = restaurantId;
        this.items = items;
        this.total = total;
        this.subtotal = total;

        if (this.items.isEmpty()) {
            throw new IllegalArgumentException("Las ordenes deben tener al menos un platillo.");
        }
    }

    public static Order create(Client client,
            UUID restaurantId,
            List<OrderItem> items,
            BigDecimal total) {
        return new Order(UUID.randomUUID(), client, restaurantId, items, total);
    }

    public void applyPromotion(String promotionId, String name, BigDecimal percentage) {
        BigDecimal amount = this.subtotal.multiply(percentage.divide(BigDecimal.valueOf(100)));
        PromotionApplied appliedPromotion = new PromotionApplied(UUID.fromString(promotionId), name, amount, percentage);
        this.total = this.total.subtract(amount);
        this.promotionApplied = appliedPromotion;
    }
}
