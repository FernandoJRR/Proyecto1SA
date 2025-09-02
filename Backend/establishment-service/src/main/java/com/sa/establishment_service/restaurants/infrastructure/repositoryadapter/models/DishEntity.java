package com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.establishment_service.restaurants.infrastructure.repositoryadapter.models.RestaurantEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "dish")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class DishEntity extends AuditorEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    public DishEntity(UUID id, String name, BigDecimal price) {
        super(id.toString());
        this.name = name;
        this.price = price;
    }

    public DishEntity(UUID id, String name, BigDecimal price, RestaurantEntity restaurant) {
        super(id.toString());
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }
}
