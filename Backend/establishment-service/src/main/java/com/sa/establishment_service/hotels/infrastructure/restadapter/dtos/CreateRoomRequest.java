package com.sa.establishment_service.hotels.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateRoomRequest {
    public String number;
    public BigDecimal pricePerNight;
    public Integer capacity;
}
