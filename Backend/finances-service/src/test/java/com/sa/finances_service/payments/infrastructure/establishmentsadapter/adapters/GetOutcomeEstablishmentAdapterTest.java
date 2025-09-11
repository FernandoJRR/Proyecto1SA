package com.sa.finances_service.payments.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import com.sa.finances_service.reports.application.dtos.OutcomeDTO;

class GetOutcomeEstablishmentAdapterTest {

    @Mock private RestClient restClient;
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersUriSpec reqUriSpec; // raw types to avoid capture issues
    @SuppressWarnings({"rawtypes","unchecked"})
    @Mock private RestClient.RequestHeadersSpec reqSpec;
    @Mock private RestClient.ResponseSpec respSpec;

    private GetOutcomeEstablishmentAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new GetOutcomeEstablishmentAdapter(restClient);
        TestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://estab");

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) reqUriSpec);
        when(reqUriSpec.uri(any(String.class))).thenReturn(reqSpec);
        when(reqSpec.retrieve()).thenReturn(respSpec);
    }

    @Test
    void getByDate_returnsOptionalWithBody_whenOk() {
        OutcomeDTO dto = new OutcomeDTO(new BigDecimal("10.00"), new BigDecimal("20.00"));
        when(respSpec.body(OutcomeDTO.class)).thenReturn(dto);

        var result = adapter.getByDate(LocalDate.of(2024,1,1), LocalDate.of(2024,1,31));

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void getByDate_returnsEmpty_onException() {
        when(respSpec.body(OutcomeDTO.class)).thenThrow(new RuntimeException("boom"));
        var result = adapter.getByDate(LocalDate.now().minusDays(1), LocalDate.now());
        assertFalse(result.isPresent());
    }
}
