package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.DishEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class RestaurantEntity extends AuditorEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = true)
    @JsonBackReference("hotel-restaurants")
    private HotelEntity hotel;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DishEntity> dishes = new ArrayList<>();

    public RestaurantEntity(UUID id, String name, String address, HotelEntity hotel) {
        super(id.toString());
        this.name = name;
        this.address = address;
        this.hotel = hotel;
    }
}
