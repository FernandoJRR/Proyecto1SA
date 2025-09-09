package com.sa.finances_service.payments.infrastructure.repositoryadapter.adapters;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.sa.finances_service.payments.application.dtos.FindPaymentsDTO;
import com.sa.finances_service.payments.application.outputports.FindPaymentsOutputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.domain.PaymentMethodEnum;
import com.sa.finances_service.payments.domain.SourceTypeEnum;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.mappers.PaymentRepositoryMapper;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.models.PaymentEntity;
import com.sa.finances_service.payments.infrastructure.repositoryadapter.repositories.PaymentRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindPaymentsAdapter implements FindPaymentsOutputPort {

    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryMapper paymentRepositoryMapper;

    @Override
    public List<Payment> findAll(FindPaymentsDTO f) {
        Specification<PaymentEntity> spec = buildSpec(f);

        return paymentRepository.findAll(spec)     // requires JpaSpecificationExecutor on the repo
                .stream()
                .map(paymentRepositoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    private Specification<PaymentEntity> buildSpec(FindPaymentsDTO f) {
        // Start with an always-true predicate to avoid null specs
        Specification<PaymentEntity> spec = (root, q, cb) -> cb.conjunction();  // recommended to avoid nulls.  [oai_citation:3‡Stack Overflow](https://stackoverflow.com/questions/60009797/jpa-specification-and-null-parameter-in-where-clause?utm_source=chatgpt.com)

        if (f.getEstablishmentId() != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("establishmentId"), f.getEstablishmentId()));
        }
        if (f.getClientId() != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("clientId"), f.getClientId()));
        }
        if (f.getSourceType() != null) {
            // expects the DTO to provide ORDER/RESERVATION as enum or string you map before calling
            spec = spec.and((r, q, cb) -> cb.equal(r.get("sourceType"), SourceTypeEnum.valueOf(f.getSourceType())));
        }
        if (f.getSourceId() != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("sourceId"), f.getSourceId()));
        }
        if (f.getMethod() != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("method"), PaymentMethodEnum.valueOf(f.getMethod())));
        }
        if (f.getPromotionId() != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("promotionId"), f.getPromotionId()));
        }
        // Date range on createdAt (inclusive). Convert LocalDate to Instant boundaries.
        if (f.getFromDate() != null) {
            Instant from = f.getFromDate().atStartOfDay().toInstant(ZoneOffset.UTC);
            spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("paidAt"), from));
        }
        if (f.getToDate() != null) {
            Instant to = f.getToDate().atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
            spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("paidAt"), to));
        }
        return spec;
    }
}
