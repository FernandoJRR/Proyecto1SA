package com.sa.establishment_service.restaurants.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.shared.domain.Establishment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Restaurant extends Establishment {
    private Hotel hotel;
    private List<Dish> dishes;

    public Restaurant(UUID id, String name, String address, Hotel hotel) {
        super(id, name, address);
        this.hotel = hotel;
    }

    public static Restaurant create(String name, String address, Hotel hotel) {
        return new Restaurant(UUID.randomUUID(), name, address, hotel);
    }
}
