package com.sa.client_service.shared.infrastructure.establishmentsadapter.adapters;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.RoomDTO;

class ExistsRoomInHotelByIdAdapterTest {

    @Mock
    private RestClient restClient;

    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock
    private RestClient.RequestHeadersUriSpec reqUriSpec;

    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock
    private RestClient.RequestHeadersSpec reqSpec;

    @Mock
    private RestClient.ResponseSpec respSpec;

    private ExistsRoomInHotelByIdAdapter adapter;
    private String establishmentServiceUrl = "http://dummy-establishments";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistsRoomInHotelByIdAdapter(restClient);
        TestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", establishmentServiceUrl);

        // stub restClient.get()
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);

        // stub .uri(template, hotelId, roomId) to return reqUriSpec
        when(reqUriSpec.uri(
            eq(establishmentServiceUrl + "/api/v1/hotels/{hotelId}/rooms/{roomId}/exists"),
            anyString(), anyString()
        )).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);

        // stub retrieve()
        when(reqUriSpec.retrieve()).thenReturn(respSpec);
    }

    @Test
    void existsById_whenRoomExists_returnsTrue() {
        String hotelId = "hotel123";
        String roomId = "room456";

        // stub toBodilessEntity to succeed
        doReturn(null).when(respSpec).toBodilessEntity();

        boolean result = adapter.existsById(hotelId, roomId);
        assertTrue(result);
    }

    @Test
    void existsById_whenRoomDoesNotExist_returnsFalse() {
        String hotelId = "hotel123";
        String roomId = "roomNotFound";

        doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null))
            .when(respSpec).toBodilessEntity();

        boolean result = adapter.existsById(hotelId, roomId);
        assertFalse(result);
    }
}

// Reflection util same as before
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