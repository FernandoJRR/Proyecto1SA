package com.sa.client_service.shared.infrastructure.promotionsadapter.adapters;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.sa.client_service.reservations.application.dtos.PromotionDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

class FindPromotionByIdAdapterTest {

    @Mock
    private RestClient restClient;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Mock
    private RestClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Mock
    private RestClient.RequestHeadersSpec headersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private FindPromotionByIdAdapter adapter;
    private String clientsServiceUrl = "http://dummy-clients";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPromotionByIdAdapter(restClient);
        TestUtils.setField(adapter, "CLIENTS_SERVICE_URL", clientsServiceUrl);

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.uri(
                eq(clientsServiceUrl + "/api/v1/promotions/{promotionId}"),
                anyString())).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void findPromotionById_whenExists_returnsOptionalWithBody() {
        String promoId = "promo123";
        PromotionDTO expected = new PromotionDTO(/* fill in expected fields */);
        // stub toEntity to return ResponseEntity with expected body
        ResponseEntity<PromotionDTO> responseEntity = ResponseEntity.ok(expected);
        when(responseSpec.toEntity(PromotionDTO.class)).thenReturn(responseEntity);

        Optional<PromotionDTO> result = adapter.findPromotionById(promoId);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void findPromotionById_whenNotFound_returnsEmpty() {
        String promoId = "promoNotPresent";

        doThrow(HttpClientErrorException.NotFound.create(
                HttpStatus.NOT_FOUND, "Not Found", null, null, null))
                .when(responseSpec).toEntity(PromotionDTO.class);

        Optional<PromotionDTO> result = adapter.findPromotionById(promoId);

        assertFalse(result.isPresent());
    }

    @Test
    void findPromotionById_whenOtherError_returnsEmpty() {
        String promoId = "promoError";

        // stub toEntity to throw HttpClientErrorException with INTERNAL_SERVER_ERROR
        doThrow(HttpClientErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null))
                .when(responseSpec).toEntity(PromotionDTO.class);

        Optional<PromotionDTO> result = adapter.findPromotionById(promoId);

        assertFalse(result.isPresent());
    }
}

// utility for setting private field
class TestUtils {
    static void setField(Object target, String fieldName, Object value) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}