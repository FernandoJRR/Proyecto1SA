package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.reservations.application.outputports.FindReservationsOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindReservationsAdapter implements FindReservationsOutputPort {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryMapper reservationRepositoryMapper;

    @Override
    public List<Reservation> findReservations(UUID hotelId, UUID roomId, String clientCui, LocalDate startDate,
            LocalDate endDate) {

        Specification<ReservationEntity> spec = Specification.where(null);

        if (hotelId != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("hotelId"), hotelId.toString()));
        }

        if (roomId != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("roomId"), roomId.toString()));
        }

        if (clientCui != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("client").get("cui"), clientCui));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, q, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), endDate),
                cb.greaterThanOrEqualTo(root.get("endDate"), startDate)
            ));
        } else if (startDate != null) {
            spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("endDate"), startDate));
        } else if (endDate != null) {
            spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("startDate"), endDate));
        }

        return reservationRepository.findAll(spec)
            .stream()
            .map(reservationRepositoryMapper::toDomain)
            .toList();
    }

}
