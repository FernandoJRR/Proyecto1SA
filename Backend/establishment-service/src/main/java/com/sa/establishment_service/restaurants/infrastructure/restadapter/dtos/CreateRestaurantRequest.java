package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantRequest {
    private String name;
    private String address;
    private String hotelId;
}
