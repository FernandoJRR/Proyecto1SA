package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.orders.application.dtos.RestaurantDTO;

@ExtendWith(MockitoExtension.class)
public class OrderHydrationAdapterTest {

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec uriSpec; // raw types
    @Mock private RestClient.RequestHeadersSpec headersSpec; // raw types
    @Mock private RestClient.ResponseSpec responseSpec;

    private OrderHydrationAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new OrderHydrationAdapter(restClient);
        ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://establishments");

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(String.class), any(Object[].class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getRestaurant_success_returnsDto() {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setName("Resto");
        when(responseSpec.body(eq(RestaurantDTO.class))).thenReturn(dto);

        RestaurantDTO result = adapter.getRestaurant("RES-1");
        assertEquals("Resto", result.getName());
    }

    @Test
    void getRestaurant_httpError_returnsNull() {
        when(responseSpec.body(eq(RestaurantDTO.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        RestaurantDTO result = adapter.getRestaurant("RES-2");
        assertNull(result);
    }
}

