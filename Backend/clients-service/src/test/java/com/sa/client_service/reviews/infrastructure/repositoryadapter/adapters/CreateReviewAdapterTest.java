package com.sa.client_service.reviews.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.client_service.clients.infrastructure.repositoryadapter.models.ClientEntity;
import com.sa.client_service.clients.infrastructure.repositoryadapter.repositories.ClientRepository;
import com.sa.client_service.reviews.domain.EstablishmentType;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.mappers.ReviewRepositoryMapper;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.models.ReviewEntity;
import com.sa.client_service.reviews.infrastructure.repositoryadapter.repositories.ReviewRepository;

@ExtendWith(MockitoExtension.class)
public class CreateReviewAdapterTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private ReviewRepositoryMapper reviewRepositoryMapper;

    private CreateReviewAdapter adapter;

    private static final String CLIENT_CUI = "1234567890123";

    @BeforeEach
    void setup() {
        adapter = new CreateReviewAdapter(reviewRepository, clientRepository, reviewRepositoryMapper);
    }

    @Test
    void givenValidInput_whenCreateReview_thenLinksClientSavesAndMaps() {
        // given
        ClientEntity client = new ClientEntity(UUID.randomUUID(), "Ana", "Lopez", "ana@example.com", CLIENT_CUI);
        when(clientRepository.findFirstByCui(CLIENT_CUI)).thenReturn(Optional.of(client));

        Review domain = Review.create("EST-1", EstablishmentType.HOTEL, "SRC-1", 5, "Great stay");
        ReviewEntity entity = new ReviewEntity();
        entity.setEstablishmentId("EST-1");
        entity.setEstablishmentType(EstablishmentType.HOTEL.name());
        entity.setSourceId("SRC-1");
        entity.setRating(5);
        entity.setComment("Great stay");

        when(reviewRepositoryMapper.toEntity(org.mockito.ArgumentMatchers.same(domain))).thenReturn(entity);
        when(reviewRepository.save(org.mockito.ArgumentMatchers.same(entity))).thenReturn(entity);

        Review mappedBack = Review.create("EST-1", EstablishmentType.HOTEL, "SRC-1", 5, "Great stay");
        when(reviewRepositoryMapper.toDomain(org.mockito.ArgumentMatchers.same(entity))).thenReturn(mappedBack);

        // when
        Review result = adapter.createReview(CLIENT_CUI, domain);

        // then
        assertAll(
            () -> assertSame(mappedBack, result),
            () -> assertSame(client, entity.getClient())
        );

        verify(clientRepository, times(1)).findFirstByCui(CLIENT_CUI);
        verify(reviewRepositoryMapper, times(1)).toEntity(org.mockito.ArgumentMatchers.same(domain));
        verify(reviewRepository, times(1)).save(org.mockito.ArgumentMatchers.same(entity));
        verify(reviewRepositoryMapper, times(1)).toDomain(org.mockito.ArgumentMatchers.same(entity));
    }

    @Test
    void givenMissingClient_whenCreateReview_thenThrowsAndDoesNotSave() {
        // given
        when(clientRepository.findFirstByCui(CLIENT_CUI)).thenReturn(Optional.empty());
        Review domain = Review.create("EST-1", EstablishmentType.RESTAURANT, "SRC-2", 4, "Tasty food");

        // when - then
        assertThrows(java.util.NoSuchElementException.class, () -> adapter.createReview(CLIENT_CUI, domain));
        verify(reviewRepositoryMapper, never()).toEntity(org.mockito.ArgumentMatchers.any());
        verify(reviewRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(reviewRepositoryMapper, never()).toDomain(org.mockito.ArgumentMatchers.any());
    }
}
