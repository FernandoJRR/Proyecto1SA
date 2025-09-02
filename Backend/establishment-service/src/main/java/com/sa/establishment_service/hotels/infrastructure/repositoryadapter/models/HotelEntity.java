package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "hotel")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class HotelEntity extends AuditorEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 250)
    private String address;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maintenanceCostPerWeek;

    /**
     * Un hotel puede tener muchas habitaciones
     */
    @OneToMany(mappedBy = "hotel")
    private List<RoomEntity> rooms;

    /**
     * Un hotel puede tener muchos restaurantes (establecimientos hijos)
     */
    @OneToMany(mappedBy = "hotel")
    @JsonManagedReference("hotel-restaurants")
    private List<RestaurantEntity> restaurants;

    public HotelEntity(String id, String name, String address, BigDecimal maintenanceCostPerWeek) {
        super(id);
        this.name = name;
        this.address = address;
        this.maintenanceCostPerWeek = maintenanceCostPerWeek;
    }
}
