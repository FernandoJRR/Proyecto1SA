package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.establishment_service.hotels.domain.RoomStatusEnum;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "room")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class RoomEntity extends AuditorEntity {
    @Column(nullable = false, length = 10, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoomStatusEnum status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelEntity hotel;

    public RoomEntity(String id, String number, BigDecimal pricePerNight, int capacity, HotelEntity hotel) {
        super(id);
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.status = RoomStatusEnum.AVAILABLE;
        this.hotel = hotel;
    }
}
