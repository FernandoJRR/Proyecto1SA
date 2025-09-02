package com.sa.client_service.reviews.domain;

import lombok.Data;

@Data
public class Rating {
    private final Integer value;

    public Rating(Integer value) {
        this.value = value;
    }

    public static Rating of(Integer value) {
        return new Rating(value);
    }
}
