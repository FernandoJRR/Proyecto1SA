package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private String id;
    private String name;
    private String address;
    private String hotelId;
}
