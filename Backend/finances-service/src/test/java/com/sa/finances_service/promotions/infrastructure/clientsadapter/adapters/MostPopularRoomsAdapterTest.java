package com.sa.finances_service.promotions.infrastructure.clientsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

class MostPopularRoomsAdapterTest {

    @Mock private RestClient restClient;
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersUriSpec reqUriSpec;
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersSpec reqSpec;
    @Mock private RestClient.ResponseSpec respSpec;

    private MostPopularRoomsAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new MostPopularRoomsAdapter(restClient);
        TestUtils.setField(adapter, "CLIENT_SERVICE_URL", "http://clients");

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);
        when(reqUriSpec.uri(any(String.class), org.mockito.ArgumentMatchers.<Object[]>any())).thenReturn(reqSpec);
        when(reqSpec.retrieve()).thenReturn(respSpec);
    }

    @Test
    void findMostPopular_limitsAndMapsList() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        UUID c = UUID.randomUUID();
        List<UUID> body = List.of(a, b, c);
        when(respSpec.body(any(ParameterizedTypeReference.class))).thenReturn(body);

        List<UUID> result = adapter.findMostPopular("h1", 1);

        assertEquals(1, result.size());
        assertEquals(a, result.get(0));
    }

    @Test
    void findMostPopular_returnsEmptyWhenNull() {
        when(respSpec.body(any(ParameterizedTypeReference.class))).thenReturn(null);
        List<UUID> result = adapter.findMostPopular("h1", 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void findMostPopular_returnsEmptyOnException() {
        when(respSpec.body(any(ParameterizedTypeReference.class))).thenThrow(new RuntimeException("boom"));
        List<UUID> result = adapter.findMostPopular("h1", 5);
        assertTrue(result.isEmpty());
    }
}

// TestUtils helper is defined in MostPopularDishesAdapterTest within this package
