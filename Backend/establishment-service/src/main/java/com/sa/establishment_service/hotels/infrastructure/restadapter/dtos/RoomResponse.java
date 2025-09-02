package com.sa.establishment_service.hotels.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RoomResponse {
    public String id;
    public String number;
    public BigDecimal pricePerNight;
    public Integer capacity;
}
