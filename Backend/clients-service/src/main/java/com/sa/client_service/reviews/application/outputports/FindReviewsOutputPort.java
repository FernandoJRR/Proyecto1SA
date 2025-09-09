package com.sa.client_service.reviews.application.outputports;

import java.util.List;

import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.domain.Review;

public interface FindReviewsOutputPort {
    public List<Review> findReviews(FindReviewsDTO dto);
}
