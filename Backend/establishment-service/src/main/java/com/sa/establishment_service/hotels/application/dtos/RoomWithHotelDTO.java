package com.sa.establishment_service.hotels.application.dtos;

import java.math.BigDecimal;

import com.sa.establishment_service.hotels.domain.RoomStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomWithHotelDTO {
    private String id;
    private String number;
    private RoomStatusEnum status;
    private BigDecimal pricePerNight;
    private int capacity;
    private String hotelId;
    private String hotelName;
}
