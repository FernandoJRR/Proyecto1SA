package com.sa.client_service.reviews.application.outputports;

import com.sa.client_service.reviews.domain.Review;

public interface CreateReviewOutputPort {
    public Review createReview(String clientCui, Review review);
}
