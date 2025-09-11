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

class MostPopularDishesAdapterTest {

    @Mock private RestClient restClient;
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersUriSpec reqUriSpec;
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersSpec reqSpec;
    @Mock private RestClient.ResponseSpec respSpec;

    private MostPopularDishesAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new MostPopularDishesAdapter(restClient);
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

        List<UUID> result = adapter.findMostPopular("r1", 2);

        assertEquals(2, result.size());
        assertEquals(a, result.get(0));
        assertEquals(b, result.get(1));
    }

    @Test
    void findMostPopular_returnsEmptyWhenNull() {
        when(respSpec.body(any(ParameterizedTypeReference.class))).thenReturn(null);
        List<UUID> result = adapter.findMostPopular("r1", 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void findMostPopular_returnsEmptyOnException() {
        when(respSpec.body(any(ParameterizedTypeReference.class))).thenThrow(new RuntimeException("boom"));
        List<UUID> result = adapter.findMostPopular("r1", 5);
        assertTrue(result.isEmpty());
    }
}

// local test util for reflection field set
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

