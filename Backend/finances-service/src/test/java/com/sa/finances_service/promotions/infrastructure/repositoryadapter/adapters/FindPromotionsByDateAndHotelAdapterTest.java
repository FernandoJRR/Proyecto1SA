package com.sa.finances_service.promotions.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories.PromotionRepository;

class FindPromotionsByDateAndHotelAdapterTest {

    @Mock private PromotionRepository promotionRepository;
    @Mock private PromotionRepositoryMapper promotionRepositoryMapper;
    private FindPromotionsByDateAndHotelAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPromotionsByDateAndHotelAdapter(promotionRepository, promotionRepositoryMapper);
    }

    @Test
    void findByStartDateAndHotel_delegatesToRepoAndMaps() {
        String hotelId = "h1";
        LocalDate at = LocalDate.now();
        List<PromotionEntity> entities = List.of(new PromotionEntity());
        List<Promotion> domain = List.of(new Promotion());
        when(promotionRepository.findActiveForHotel(hotelId, at)).thenReturn(entities);
        when(promotionRepositoryMapper.toDomain(entities)).thenReturn(domain);

        List<Promotion> result = adapter.findByStartDateAndHotel(at, hotelId);
        assertEquals(domain, result);
    }
}

