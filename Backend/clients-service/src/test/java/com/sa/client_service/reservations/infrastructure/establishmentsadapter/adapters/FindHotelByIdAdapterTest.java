package com.sa.client_service.reservations.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.HotelDTO;

@ExtendWith(MockitoExtension.class)
public class FindHotelByIdAdapterTest {

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec uriSpec; // raw types to sidestep generics capture
    @Mock private RestClient.RequestHeadersSpec headersSpec; // raw types to sidestep generics capture
    @Mock private RestClient.ResponseSpec responseSpec;

    private FindHotelByIdAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindHotelByIdAdapter(restClient);
        ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://establishments");

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(String.class), any(Object[].class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void givenExistingHotel_whenFindById_thenReturnsOptionalPresent() {
        HotelDTO dto = new HotelDTO();
        dto.setId("H-1");
        when(responseSpec.body(eq(HotelDTO.class))).thenReturn(dto);

        Optional<HotelDTO> result = adapter.findById("H-1");

        assertTrue(result.isPresent());
        assertSame(dto, result.get());
    }

    @Test
    void givenHttpError_whenFindById_thenReturnsEmpty() {
        when(responseSpec.body(eq(HotelDTO.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<HotelDTO> result = adapter.findById("H-2");

        assertFalse(result.isPresent());
    }
}
