package com.sa.client_service.reservations.infrastructure.establishmentsadapter.adapters;

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

import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;

@ExtendWith(MockitoExtension.class)
public class ReservationHydrationAdapterTest {

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec uriSpec; // raw types to avoid generic capture issues
    @Mock private RestClient.RequestHeadersSpec headersSpec; // raw types to avoid generic capture issues
    @Mock private RestClient.ResponseSpec responseSpec;

    private ReservationHydrationAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new ReservationHydrationAdapter(restClient);
        ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://establishments");

        when(restClient.get()).thenReturn(uriSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getHotel_success_returnsDto() {
        when(uriSpec.uri(any(String.class), any(Object[].class))).thenReturn(headersSpec);
        HotelDTO dto = new HotelDTO();
        dto.setId("H-1");
        when(responseSpec.body(eq(HotelDTO.class))).thenReturn(dto);

        HotelDTO result = adapter.getHotel("H-1");
        assertEquals("H-1", result.getId());
    }

    @Test
    void getHotel_httpError_returnsNull() {
        when(uriSpec.uri(any(String.class), any(Object[].class))).thenReturn(headersSpec);
        when(responseSpec.body(eq(HotelDTO.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        HotelDTO result = adapter.getHotel("H-2");
        assertNull(result);
    }

    @Test
    void getRoom_success_returnsDto() {
        when(uriSpec.uri(any(String.class), any(Object[].class))).thenReturn(headersSpec);
        RoomDTO dto = new RoomDTO();
        dto.setId("R-1");
        when(responseSpec.body(eq(RoomDTO.class))).thenReturn(dto);

        RoomDTO result = adapter.getRoom("H-1", "R-1");
        assertEquals("R-1", result.getId());
    }

    @Test
    void getRoom_httpError_returnsNull() {
        when(uriSpec.uri(any(String.class), any(Object[].class))).thenReturn(headersSpec);
        when(responseSpec.body(eq(RoomDTO.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        RoomDTO result = adapter.getRoom("H-1", "R-2");
        assertNull(result);
    }
}
