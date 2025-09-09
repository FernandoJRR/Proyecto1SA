package com.sa.client_service.reviews.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.application.outputports.FindReviewsOutputPort;
import com.sa.client_service.reviews.domain.EstablishmentType;
import com.sa.client_service.reviews.domain.Rating;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.mappers.ReviewRepositoryMapper;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.repositories.ReviewRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindReviewsAdapter implements FindReviewsOutputPort {

    private final ReviewRepository reviewRepository;
    private final ReviewRepositoryMapper reviewRepositoryMapper;

    @Override
    public List<Review> findReviews(FindReviewsDTO filter) {
        Specification<ReviewEntity> spec = buildSpec(filter);

        return reviewRepository
                .findAll(spec)
                .stream()
                .map(reviewRepositoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    private Specification<ReviewEntity> buildSpec(FindReviewsDTO f) {
            return Specification.<ReviewEntity>where((root, query, cb) -> null)
            .and(f.getEstablishmentId() != null ?
                (root, q, cb) -> cb.equal(root.get("establishmentId"), f.getEstablishmentId()) : null)
            .and(f.getEstablishmentType() != null ?
                (root, q, cb) -> cb.equal(root.get("establishmentType"), f.getEstablishmentType()) : null)
            .and(f.getSourceId() != null ?
                (root, q, cb) -> cb.equal(root.get("sourceId"), f.getSourceId()) : null)
            .and(f.getClientId() != null ?
                (root, q, cb) -> cb.equal(root.get("clientId"), f.getClientId()) : null);
    }
}
