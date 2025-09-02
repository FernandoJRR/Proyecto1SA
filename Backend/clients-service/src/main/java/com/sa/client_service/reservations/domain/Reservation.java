package com.sa.client_service.reservations.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.shared.domain.PromotionApplied;
import com.sa.client_service.shared.domain.Promotionable;
import com.sa.domain.Auditor;
import com.sa.shared.exceptions.IllegalArgumentException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Reservation extends Auditor implements Promotionable {
    private Client client;
    private UUID hotelId;
    private UUID roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;

    private PromotionApplied promotionApplied;

    private BigDecimal totalCost;
    private BigDecimal subtotal;

    public Reservation(UUID id, Client client, UUID hotelId, UUID roomId,
            LocalDate start, LocalDate end, PromotionApplied promotionApplied,
            BigDecimal subtotal, BigDecimal totalCost,
            ReservationStatus reservationStatus) {
        super(id);
        this.client = client;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.startDate = start;
        this.endDate = end;
        this.subtotal = subtotal;
        this.totalCost = totalCost;
        this.promotionApplied = promotionApplied;
        this.status = reservationStatus;
    }

    public static Reservation create(Client client, UUID hotelId, UUID roomId,
            LocalDate start, LocalDate end, BigDecimal nightPrice,
            Integer reservationDays) {

        BigDecimal subtotal = nightPrice.multiply(BigDecimal.valueOf(reservationDays));
        BigDecimal total = subtotal;

        return new Reservation(
                UUID.randomUUID(),
                client,
                hotelId,
                roomId,
                start,
                end,
                null,
                subtotal,
                total,
                ReservationStatus.CONFIRMED);
    }

    public void applyPromotion(String promotionId, String name, BigDecimal percentage) {
        BigDecimal amount = this.subtotal.multiply(percentage.divide(BigDecimal.valueOf(100)));
        PromotionApplied appliedPromotion = new PromotionApplied(UUID.fromString(promotionId), name, amount, percentage);
        this.totalCost = this.totalCost.subtract(amount);
        this.promotionApplied = appliedPromotion;
    }

    public void checkIn(LocalDate today) throws IllegalArgumentException {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("La reservacion no ha sido confirmada");
        }
        this.status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut(LocalDate today) throws IllegalArgumentException {
        if (status != ReservationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("El cliente no ha hecho Check-In");
        }
        if (!today.isBefore(startDate)) {

        }
        this.status = ReservationStatus.CHECKED_OUT;
    }
}
