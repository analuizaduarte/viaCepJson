package org.viacep.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.viacep.entity.dto.UfDto;
import org.viacep.entity.dto.response.CepResponse;
import org.viacep.entity.dto.response.LogResponse;
import org.viacep.repository.CepRepository;
import org.viacep.service.impl.CepServiceImpl;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CepServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private CepRepository cepRepository;

    @InjectMocks
    private CepServiceImpl cepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testGetCepSuccess() throws Exception {
        CepResponse resultadoEsperado = new CepResponse();
        resultadoEsperado.setCep("37550-110");
        resultadoEsperado.setRua("Avenida Doutor Lisboa");
        resultadoEsperado.setBairro("Centro");
        resultadoEsperado.setCidade("Pouso Alegre");
        resultadoEsperado.setUf("MG");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CepResponse.class)).thenReturn(Mono.just(resultadoEsperado));

        CepResponse resultadoReal = cepService.getCep("37550110");

        assertNotNull(resultadoReal);
        assertEquals(resultadoEsperado.getCep(), resultadoReal.getCep());
        assertEquals(resultadoEsperado.getRua(), resultadoReal.getRua());
        assertEquals(resultadoEsperado.getBairro(), resultadoReal.getBairro());
        assertEquals(resultadoEsperado.getCidade(), resultadoReal.getCidade());
        assertEquals(resultadoReal.getUf(), resultadoReal.getUf());

        verify(cepRepository, times(1)).save(any(UfDto.class));
    }

    @Test
    void testGetCepNotFound() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CepResponse.class)).thenReturn(Mono.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cepService.getCep("00000000");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testGetCepIsEmpty() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CepResponse.class)).thenReturn(Mono.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cepService.getCep("");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testGetCepExceptionHandling() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CepResponse.class)).thenThrow(WebClientResponseException.class);

        assertThrows(RuntimeException.class, () -> {
            cepService.getCep("37550110");
        });
    }

    @Test
    void testGetListaCepsSuccess() throws Exception {
        UfDto insertLog1 = new UfDto();
        insertLog1.setId(1);
        insertLog1.setUf("MG");
        insertLog1.setCep("37550-110");
        insertLog1.setDtHrConsulta(LocalDate.now());

        UfDto insertLog2 = new UfDto();
        insertLog2.setId(2);
        insertLog2.setUf("MG");
        insertLog2.setCep("37558-513");
        insertLog2.setDtHrConsulta(LocalDate.now());

        LogResponse logResponse1 = new LogResponse(insertLog1.getCep(), insertLog1.getDtHrConsulta());
        LogResponse logResponse2 = new LogResponse(insertLog2.getCep(), insertLog2.getDtHrConsulta());

        List<LogResponse> resultadoEsperado = Arrays.asList(logResponse1, logResponse2);

        when(cepRepository.findListCep("MG")).thenReturn(resultadoEsperado);

        List<LogResponse> resultadoReal = cepService.getListaCeps("MG");

        assertNotNull(resultadoReal);
        assertEquals(resultadoEsperado.size(), resultadoReal.size());
        assertEquals(resultadoEsperado.get(0), resultadoReal.get(0));
        assertEquals(resultadoEsperado.get(1), resultadoReal.get(1));

        verify(cepRepository).findListCep("MG");
    }

    @Test
    void testGetListaCepsBadRequest() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cepService.getListaCeps("");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("O parâmetro 'uf' é obrigatório.", exception.getReason());
    }

    @Test
    void testGetListaCepsExceptionHandling() {
        when(cepRepository.findListCep("37550110")).thenThrow(new RuntimeException("Erro inesperado ao buscar o CEP"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cepService.getListaCeps("37550110");
        });

        assertThrows(RuntimeException.class, () -> {
            cepService.getListaCeps("37550110");
        });
        assertEquals("Erro inesperado ao buscar o CEP", exception.getMessage());
    }
}