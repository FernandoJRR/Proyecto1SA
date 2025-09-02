package com.sa.finances_service.payments.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethodEnum {
    CASH("Efectivo"),
    CARD("Tarjeta");

    private final String name;

    public String getName() {
        return name;
    }
}
