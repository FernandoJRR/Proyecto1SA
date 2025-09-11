package com.sa.client_service.reviews.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.domain.EstablishmentType;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.mappers.ReviewRepositoryMapper;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.repositories.ReviewRepository;

@ExtendWith(MockitoExtension.class)
public class FindReviewsAdapterTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private ReviewRepositoryMapper reviewRepositoryMapper;

    private FindReviewsAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindReviewsAdapter(reviewRepository, reviewRepositoryMapper);
    }

    @Test
    void givenFilters_whenFindReviews_thenReturnsMappedDomains() {
        // given
        FindReviewsDTO dto = FindReviewsDTO.builder()
            .establishmentId("EST-123")
            .establishmentType(EstablishmentType.HOTEL.name())
            .sourceId("SRC-999")
            .build();

        ReviewEntity e1 = new ReviewEntity();
        e1.setEstablishmentId("EST-123");
        e1.setEstablishmentType(EstablishmentType.HOTEL.name());
        e1.setSourceId("SRC-999");
        e1.setRating(5);
        e1.setComment("Great");

        ReviewEntity e2 = new ReviewEntity();
        e2.setEstablishmentId("EST-123");
        e2.setEstablishmentType(EstablishmentType.HOTEL.name());
        e2.setSourceId("SRC-999");
        e2.setRating(5);
        e2.setComment("Great");

        when(reviewRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(e1, e2));

        Review d1 = Review.create("EST-123", EstablishmentType.HOTEL, "SRC-999", 5, "Great");
        Review d2 = Review.create("EST-123", EstablishmentType.HOTEL, "SRC-999", 5, "Great");
        when(reviewRepositoryMapper.toDomain(e1)).thenReturn(d1);
        when(reviewRepositoryMapper.toDomain(e2)).thenReturn(d2);

        // when
        List<Review> results = adapter.findReviews(dto);

        // then
        assertEquals(2, results.size());
        // Order from repository is not guaranteed; assert by content
        boolean hasGreat = results.stream().anyMatch(r -> "Great".equals(r.getComment()) && r.getRating().getValue() == 5);
        boolean hasGreat2 = results.stream().anyMatch(r -> "Great".equals(r.getComment()) && r.getRating().getValue() == 5);
        assertTrue(hasGreat);
        assertTrue(hasGreat2);
        verify(reviewRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void givenNoMatches_whenFindReviews_thenReturnsEmptyList() {
        // given
        FindReviewsDTO dto = FindReviewsDTO.builder().establishmentId("EST-X").build();
        when(reviewRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        // when
        List<Review> results = adapter.findReviews(dto);

        // then
        assertTrue(results.isEmpty());
        verify(reviewRepository, times(1)).findAll(any(Specification.class));
    }
}
