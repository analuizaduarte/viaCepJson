package org.viacep.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.viacep.entity.dto.UfDto;
import org.viacep.entity.dto.response.CepResponse;
import org.viacep.entity.dto.response.LogResponse;
import org.viacep.repository.CepRepository;
import org.viacep.service.CepService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
            CepResponse cepResponse =
                    this.webClient.get()
                            .uri("/ws/{cep}/json", cep)
                            .retrieve()
                            .bodyToMono(CepResponse.class)
                            .block();

            if (cepResponse == null || cepResponse.getCep() == null) {
                throw new IllegalArgumentException("CEP não encontrado: " + cep);
            }

            UfDto insertLog = new UfDto();
            insertLog.setUf(cepResponse.getUf());
            insertLog.setCep(cepResponse.getCep());
            insertLog.setDtHrConsulta(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            cepRepository.save(insertLog);

            return cepResponse;

        }
        catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar o CEP", e);
        }
    }

    @Override
    public List<LogResponse> getListaCeps(String uf) {
        try {
            List<LogResponse> logResponseList = cepRepository.findListCep(uf);
            return  logResponseList.size() > 20 ? logResponseList.subList(0, 20) : logResponseList;
        }
        catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar UF", e);
        }
    }
}
