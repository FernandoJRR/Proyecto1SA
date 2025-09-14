package com.sa.client_service.reviews.infrastructure.establishementsadapter.adapters;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reviews.infrastructure.establishementsadapter.adapters.ExistsRoomByIdAdapter;

class ExistsRoomByIdAdapterTest {

    @Mock
    private RestClient restClient;

    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock
    private RestClient.RequestHeadersUriSpec reqUriSpec;

    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock
    private RestClient.RequestHeadersSpec headersSpec;

    @Mock
    private RestClient.ResponseSpec respSpec;

    private ExistsRoomByIdAdapter adapter;
    private String establishmentsServiceUrl = "http://dummy-establishments";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistsRoomByIdAdapter(restClient);
        TestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", establishmentsServiceUrl);

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);
        when(reqUriSpec.uri(
            eq(establishmentsServiceUrl + "/api/v1/hotels/rooms/{roomId}/exists"),
            anyString()
        )).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);
        when(reqUriSpec.retrieve()).thenReturn(respSpec);
    }

    @Test
    void existsById_whenRoomExists_returnsTrue() {
        String roomId = "room123";
        // stub toBodilessEntity to succeed (no exception)
        doReturn(null).when(respSpec).toBodilessEntity();

        boolean result = adapter.existsById(roomId);
        assertTrue(result);
    }

    @Test
    void existsById_whenRoomNotFound_returnsFalse() {
        String roomId = "roomNotFound";
        // stub toBodilessEntity to throw NotFound
        doThrow(HttpClientErrorException.NotFound.create(
            HttpStatus.NOT_FOUND, "Not Found", null, null, null))
            .when(respSpec).toBodilessEntity();

        boolean result = adapter.existsById(roomId);
        assertFalse(result);
    }

    @Test
    void existsById_whenOtherHttpClientError_throwsException() {
        String roomId = "roomError";
        // stub toBodilessEntity to throw some other HttpClientErrorException
        doThrow(HttpClientErrorException.create(
            HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null))
            .when(respSpec).toBodilessEntity();

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> adapter.existsById(roomId));
        // optional: check status or message
        assertTrue(ex.getMessage().contains("500"));
    }
}

// Reflection helper for tests
class TestUtils {
    static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}