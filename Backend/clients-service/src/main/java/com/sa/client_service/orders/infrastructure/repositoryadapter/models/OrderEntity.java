package com.sa.client_service.orders.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.shared.infrastructure.repositoryadapter.models.PromotionAppliedEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderEntity extends AuditorEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "orderedAt", nullable = false)
    private LocalDate orderedAt;

    @Embedded
    private PromotionAppliedEntity promotionApplied;
}
