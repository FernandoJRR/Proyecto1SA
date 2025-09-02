package com.sa.client_service.reviews.infrastructure.restadapter.mapper;

import org.mapstruct.Mapper;

import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.domain.Rating;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.CreateReviewRequest;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.ReviewResponse;

@Mapper(componentModel = "spring")
public interface ReviewRestMapper {
    public CreateReviewDTO toDTO(CreateReviewRequest createReviewRequest);
    public ReviewResponse toResponse(Review review);

    default Integer toEntityRating(Rating rating) {
        return (rating == null) ? null : rating.getValue();
    }

    default Rating toDomainRating(Integer value) {
        if (value == null) return null;
        Rating rating = new Rating(value);
        return rating;
    }
}
