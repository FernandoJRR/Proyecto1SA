package com.sa.establishment_service.hotels.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room extends Auditor {
    private String number;
    private RoomStatusEnum status;
    private BigDecimal pricePerNight;
    private int capacity;

    public Room(UUID id, String number, BigDecimal pricePerNight, int capacity, RoomStatusEnum status) {
        super(id);
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.status = status;
    }

    public static Room create(String number, BigDecimal pricePerNight, int capacity) {
        return new Room(UUID.randomUUID(), number, pricePerNight, capacity, RoomStatusEnum.AVAILABLE);
    }

    public void changePrice(BigDecimal newPrice) {
        this.pricePerNight = newPrice;
    }

    public void markAvailable() {
        this.status = RoomStatusEnum.AVAILABLE;
    }

    public void markUnavailable() {
        this.status = RoomStatusEnum.UNAVAILABLE;
    }
}
