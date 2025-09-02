package com.sa.client_service.reviews.infrastructure.repositoryadapter.adapters;

import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.clients.infrastructure.repositoryadapter.repositories.ClientRepository;
import com.sa.client_service.reviews.application.outputports.CreateReviewOutputPort;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.mappers.ReviewRepositoryMapper;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.repositories.ReviewRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateReviewAdapter implements CreateReviewOutputPort {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final ReviewRepositoryMapper reviewRepositoryMapper;

    @Override
    public Review createReview(String clientCui, Review review) {
        ClientEntity foundClient = clientRepository.findFirstByCui(clientCui).get();
        ReviewEntity reviewEntity = reviewRepositoryMapper.toEntity(review);
        reviewEntity.setClient(foundClient);
        return reviewRepositoryMapper.toDomain(reviewRepository.save(reviewEntity));
    }

}
