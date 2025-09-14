package com.sa.client_service.reservations.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers.ReservationRepositoryMapper;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories.ReservationRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class FindReservationsAdapterTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationRepositoryMapper reservationRepositoryMapper;

    private FindReservationsAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindReservationsAdapter(reservationRepository, reservationRepositoryMapper);
    }

    @Test
    void givenFilters_whenFindReservations_thenReturnsMappedDomains() {
        UUID hotelId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        String cui = "1234567890123";
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);

        ReservationEntity e1 = new ReservationEntity();

        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Arrays.asList(e1));

        Reservation d1 = org.mockito.Mockito.mock(Reservation.class);
        Reservation d2 = org.mockito.Mockito.mock(Reservation.class);
        when(reservationRepositoryMapper.toDomain(e1)).thenReturn(d1);

        List<Reservation> results = adapter.findReservations(hotelId, roomId, cui, start, end);

        assertEquals(1, results.size());
        assertTrue(results.contains(d1));
        verify(reservationRepository, times(1)).findAll(any(Specification.class));
        verify(reservationRepositoryMapper, times(1)).toDomain(e1);
    }

    @Test
    void givenNoMatches_whenFindReservations_thenReturnsEmptyList() {
        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Collections.emptyList());

        List<Reservation> results = adapter.findReservations(null, null, null, null, null);

        assertTrue(results.isEmpty());
        verify(reservationRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void givenStartAndEnd_whenFindReservations_buildsOverlappingDateSpec() {
        LocalDate start = LocalDate.of(2024, 1, 10);
        LocalDate end = LocalDate.of(2024, 1, 20);

        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Collections.emptyList());

        adapter.findReservations(null, null, null, start, end);

        ArgumentCaptor<Specification<ReservationEntity>> captor = ArgumentCaptor.forClass(Specification.class);
        verify(reservationRepository).findAll(captor.capture());

        Specification<ReservationEntity> spec = captor.getValue();

        Root<ReservationEntity> root = Mockito.mock(Root.class, Mockito.RETURNS_DEEP_STUBS);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

        Predicate p1 = Mockito.mock(Predicate.class);
        Predicate p2 = Mockito.mock(Predicate.class);
        Predicate pAnd = Mockito.mock(Predicate.class);

        when(cb.lessThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(end))).thenReturn(p1);
        when(cb.greaterThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(start))).thenReturn(p2);
        when(cb.and(p1, p2)).thenReturn(pAnd);

        Predicate built = spec.toPredicate(root, query, cb);

        assertTrue(built == pAnd);
        verify(cb).lessThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(end));
        verify(cb).greaterThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(start));
        verify(cb).and(p1, p2);
    }

    @Test
    void givenOnlyStart_whenFindReservations_buildsEndAfterOrEqualSpec() {
        LocalDate start = LocalDate.of(2024, 2, 1);

        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Collections.emptyList());

        adapter.findReservations(null, null, null, start, null);

        ArgumentCaptor<Specification<ReservationEntity>> captor = ArgumentCaptor.forClass(Specification.class);
        verify(reservationRepository).findAll(captor.capture());

        Specification<ReservationEntity> spec = captor.getValue();

        Root<ReservationEntity> root = Mockito.mock(Root.class, Mockito.RETURNS_DEEP_STUBS);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

        Predicate p = Mockito.mock(Predicate.class);
        when(cb.greaterThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(start))).thenReturn(p);

        Predicate built = spec.toPredicate(root, query, cb);
        assertTrue(built == p);
        verify(cb).greaterThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(start));
        Mockito.verify(cb, Mockito.never()).lessThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), any(LocalDate.class));
    }

    @Test
    void givenOnlyEnd_whenFindReservations_buildsStartBeforeOrEqualSpec() {
        LocalDate end = LocalDate.of(2024, 3, 15);

        when(reservationRepository.findAll(any(Specification.class)))
            .thenReturn(Collections.emptyList());

        adapter.findReservations(null, null, null, null, end);

        ArgumentCaptor<Specification<ReservationEntity>> captor = ArgumentCaptor.forClass(Specification.class);
        verify(reservationRepository).findAll(captor.capture());

        Specification<ReservationEntity> spec = captor.getValue();

        Root<ReservationEntity> root = Mockito.mock(Root.class, Mockito.RETURNS_DEEP_STUBS);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

        Predicate p = Mockito.mock(Predicate.class);
        when(cb.lessThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(end))).thenReturn(p);

        Predicate built = spec.toPredicate(root, query, cb);
        assertTrue(built == p);
        verify(cb).lessThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), eq(end));
        Mockito.verify(cb, Mockito.never()).greaterThanOrEqualTo(Mockito.<Expression<LocalDate>>any(), any(LocalDate.class));
    }
}
