package com.sa.client_service.reservations.infrastructure.repositoryadapter.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.reservations.domain.ReservationStatus;
import com.sa.client_service.shared.infrastructure.repositoryadapter.models.PromotionAppliedEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
public class ReservationEntity extends AuditorEntity {
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "hotel_id", nullable = false)
    private UUID hotelId;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Embedded
    private PromotionAppliedEntity promotionAppliedEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;
}
