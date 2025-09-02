package com.sa.client_service.reviews.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.client_service.reviews.domain.Rating;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewRepositoryMapper {
    public ReviewEntity toEntity(Review review);
    public Review toDomain(ReviewEntity reviewEntity);
    default Integer toEntityRating(Rating rating) {
        return (rating == null) ? null : rating.getValue();
    }

    default Rating toDomainRating(Integer value) {
        if (value == null) return null;
        Rating rating = new Rating(value);
        return rating;
    }
}
