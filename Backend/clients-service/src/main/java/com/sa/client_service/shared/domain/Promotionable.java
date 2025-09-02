package com.sa.client_service.shared.domain;

import java.math.BigDecimal;

public interface Promotionable {
    public void applyPromotion(String promotionId, String name, BigDecimal percentage);
}
