package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class ExistsRestaurantByIdAdapterTest {

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec uriSpec; // raw types
    @Mock private RestClient.RequestHeadersSpec headersSpec; // raw types
    @Mock private RestClient.ResponseSpec responseSpec;

    private ExistsRestaurantByIdAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new ExistsRestaurantByIdAdapter(restClient);
        ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://establishments");

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(Object[].class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void givenExistingRestaurant_whenExistsById_thenReturnsTrue() {
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());
        boolean exists = adapter.existsById("RES-1");
        assertTrue(exists);
    }
}
