package org.viacep.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.viacep.entity.dto.UfDto;
import org.viacep.entity.dto.response.CepResponse;
import org.viacep.entity.dto.response.LogResponse;
import org.viacep.repository.CepRepository;
import org.viacep.service.CepService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CepServiceImpl implements CepService {

    private final WebClient webClient;
    private final CepRepository cepRepository;

    @Autowired
    public CepServiceImpl(WebClient.Builder webClientBuilder, CepRepository cepRepository) {
        this.webClient = webClientBuilder.baseUrl("https://viacep.com.br/").build();
        this.cepRepository = cepRepository;
    }
    @Override
    public CepResponse getCep(String cep) {
        try {
            if (cep == null || cep.trim().isEmpty() ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CEP não foi preenchido.");
            }
            CepResponse cepResponse =
                    this.webClient.get()
                            .uri("/ws/{cep}/json", cep)
                            .retrieve()
                            .bodyToMono(CepResponse.class)
                            .block();

            if (cepResponse == null || cepResponse.getCep() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado: " + cep);
            }

            UfDto insertLog = new UfDto();
            insertLog.setUf(cepResponse.getUf());
            insertLog.setCep(cepResponse.getCep());
            insertLog.setDtHrConsulta(LocalDate.now());

            cepRepository.save(insertLog);

            return cepResponse;

        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar o CEP", e);
        }
    }

    @Override
    public List<LogResponse> getListaCeps(String uf) {
        if (uf == null || uf.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O parâmetro 'uf' é obrigatório.");
        }
        try {
            List<LogResponse> logResponseList = cepRepository.findListCep(uf);
            return  logResponseList.size() > 20 ? logResponseList.subList(0, 20) : logResponseList;
        }
        catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar o CEP", e);
        }
    }
}
