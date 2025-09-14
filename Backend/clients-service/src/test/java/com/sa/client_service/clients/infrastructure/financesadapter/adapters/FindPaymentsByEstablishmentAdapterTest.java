package com.sa.client_service.clients.infrastructure.financesadapter.adapters;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.sa.client_service.clients.application.dtos.PaymentDTO;
import com.sa.client_service.clients.infrastructure.financesadapter.adapters.FindPaymentsByEstablishmentAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.core.ParameterizedTypeReference;

class FindPaymentsByEstablishmentAdapterTest {

    @Mock
    private RestClient restClient;

    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock
    private RestClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock
    private RestClient.RequestHeadersSpec headersSpec;  // not used but kept if needed

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private FindPaymentsByEstablishmentAdapter adapter;

    private String financesServiceUrl = "http://dummy-finances";

    private java.util.UUID establishmentId = java.util.UUID.randomUUID();
    private java.util.UUID clientId = java.util.UUID.randomUUID();
    private java.util.UUID sourceId = java.util.UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindPaymentsByEstablishmentAdapter(restClient);
        TestUtils.setField(adapter, "FINANCES_SERVICE_URL", financesServiceUrl);

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.uri(any(String.class))).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void findByClient_returnsList_whenRemoteOk() {
        // Arrange
        LocalDate now = LocalDate.now();
        PaymentDTO dto1 = new PaymentDTO(
            establishmentId,
            clientId,
            "CARD",
            sourceId,
            new BigDecimal("100.00"),
            new BigDecimal("10.00"),
            new BigDecimal("90.00"),
            now,
            "Test Payment 1"
        );
        PaymentDTO dto2 = new PaymentDTO(
            establishmentId,
            clientId,
            "CARD",
            sourceId,
            new BigDecimal("50.00"),
            new BigDecimal("5.00"),
            new BigDecimal("45.00"),
            now.minusDays(1),
            "Test Payment 2"
        );
        List<PaymentDTO> returned = List.of(dto1, dto2);

        when(responseSpec.body(new ParameterizedTypeReference<List<PaymentDTO>>() {}))
            .thenReturn(returned);

        // Act
        List<PaymentDTO> result = adapter.findByClient(clientId.toString(), establishmentId.toString(), now.minusDays(2), now);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void findByClient_returnsEmptyList_onHttpClientErrorException() {
        // Arrange
        when(responseSpec.body(new ParameterizedTypeReference<List<PaymentDTO>>() {}))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        List<PaymentDTO> result = adapter.findByClient(clientId.toString(), establishmentId.toString(), null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByClient_handlesNullEstablishmentId_andDates() {
        // Arrange
        LocalDate now = LocalDate.now();
        PaymentDTO dto = new PaymentDTO(
            establishmentId,
            clientId,
            "CASH",
            sourceId,
            new BigDecimal("20.00"),
            new BigDecimal("0.00"),
            new BigDecimal("20.00"),
            now,
            "No establishmentId, null dates"
        );
        List<PaymentDTO> returned = List.of(dto);

        when(responseSpec.body(new ParameterizedTypeReference<List<PaymentDTO>>() {}))
            .thenReturn(returned);

        // Act
        List<PaymentDTO> result = adapter.findByClient(clientId.toString(), null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}

// Reflection util to set private field
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