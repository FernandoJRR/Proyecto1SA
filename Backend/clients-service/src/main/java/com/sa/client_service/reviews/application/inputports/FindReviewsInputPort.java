package com.sa.client_service.reviews.application.inputports;

import java.util.List;

import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.domain.Review;

public interface FindReviewsInputPort {
    public List<Review> handle(FindReviewsDTO dto);
}
