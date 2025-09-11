package com.sa.finances_service.payments.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import com.sa.finances_service.payments.application.dtos.HotelDTO;
import com.sa.finances_service.payments.application.dtos.RestaurantDTO;

class PaymentHydrationAdapterTest {

    @Mock private RestClient restClient;
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersUriSpec reqUriSpec; // use raw to avoid wildcard capture issues
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersSpec reqSpec; // use raw to avoid wildcard capture issues
    @Mock private RestClient.ResponseSpec respSpec;

    private PaymentHydrationAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new PaymentHydrationAdapter(restClient);
        // simulate property injection
        TestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://estab");

        // avoid generics capture issues by returning raw types
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);
        // disambiguate varargs overload by matching Object[] explicitly
        when(reqUriSpec.uri(any(String.class), org.mockito.ArgumentMatchers.<Object[]>any())).thenReturn(reqSpec);
        when(reqSpec.retrieve()).thenReturn(respSpec);
    }

    @Test
    void getHotel_returnsDto_whenRemoteOk() {
        HotelDTO dto = new HotelDTO("h1", "H", "A", null);
        when(respSpec.body(HotelDTO.class)).thenReturn(dto);

        HotelDTO result = adapter.getHotel("h1");
        assertEquals(dto, result);
    }

    @Test
    void getHotel_returnsNull_onException() {
        when(respSpec.body(HotelDTO.class)).thenThrow(new RuntimeException("boom"));
        HotelDTO result = adapter.getHotel("h1");
        assertNull(result);
    }

    @Test
    void getRestaurant_returnsDto_whenRemoteOk() {
        // set up chain for restaurant as well (same mocks reused)
        when(reqUriSpec.uri(any(String.class), org.mockito.ArgumentMatchers.<Object[]>any())).thenReturn(reqSpec);
        when(reqSpec.retrieve()).thenReturn(respSpec);

        RestaurantDTO dto = new RestaurantDTO("R", "Addr");
        when(respSpec.body(RestaurantDTO.class)).thenReturn(dto);

        RestaurantDTO result = adapter.getRestaurant("r1");
        assertEquals(dto, result);
    }

    @Test
    void getRestaurant_returnsNull_onException() {
        when(respSpec.body(RestaurantDTO.class)).thenThrow(new RuntimeException("boom"));
        RestaurantDTO result = adapter.getRestaurant("r1");
        assertNull(result);
    }
}

// Minimal reflection util to set private field without adding dependencies
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
