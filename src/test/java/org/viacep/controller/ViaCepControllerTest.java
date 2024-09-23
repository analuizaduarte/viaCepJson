package org.viacep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.viacep.entity.dto.response.CepResponse;
import org.viacep.entity.dto.response.LogResponse;
import org.viacep.service.CepService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ViaCepControllerTest {

    @InjectMocks
    private ViaCepController viaCepController;

    @Mock
    private CepService cepService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(viaCepController).build();
    }

    @Test
    void testGetCepSuccess() throws Exception {
        CepResponse resultadoEsperado = new CepResponse();
        resultadoEsperado.setCep("37550-110");
        resultadoEsperado.setRua("Avenida Doutor Lisboa");
        resultadoEsperado.setBairro("Centro");
        resultadoEsperado.setCidade("Pouso Alegre");
        resultadoEsperado.setUf("MG");

        when(cepService.getCep(anyString())).thenReturn(resultadoEsperado);

        ObjectMapper objectMapper = new ObjectMapper();
        String resultadoEsperadoJson = objectMapper.writeValueAsString(resultadoEsperado);

        mockMvc.perform(get("/api/consulta-cep/37550110")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().json(resultadoEsperadoJson));
    }

    @Test
    void testGetCepNotFound() throws Exception{
        when(this.cepService.getCep(anyString())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/consulta-cep/00000000")).andExpect(status().isNotFound());
    }

    @Test
    void testGetCepBadRequest() throws Exception {
        when(this.cepService.getCep(anyString())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/api/consulta-cep/aa")).andExpect(status().isBadRequest());
    }


    @Test
    void testGetListaCepsSuccess() throws Exception {
        List<LogResponse> resultadoEsperado = new ArrayList<>();
        LogResponse logResponse1 = new LogResponse("37558-513", LocalDate.parse("2024-09-21"));
        LogResponse logResponse2 = new LogResponse("37550-110", LocalDate.parse("2024-09-22"));
        resultadoEsperado.add(logResponse1);
        resultadoEsperado.add(logResponse2);

        when(cepService.getListaCeps(anyString())).thenReturn(resultadoEsperado);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String resultadoEsperadoJson = objectMapper.writeValueAsString(resultadoEsperado);

        mockMvc.perform(get("/api/lista-ceps")
                        .param("uf", "MG")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().json(resultadoEsperadoJson));
    }

    @Test
    void testGetListaCepsEmpty() throws Exception {
        when(cepService.getListaCeps(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/lista-ceps")
                        .param("uf", "MG")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetListaCepsBadRequest() throws Exception {
        when(this.cepService.getListaCeps(anyString())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/api/lista-ceps")
                        .param("uf", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}