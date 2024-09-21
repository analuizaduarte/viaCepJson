package org.viacep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.viacep.entity.dto.response.CepResponse;

@RestController
@RequestMapping("/api")
public class ViaCepController {

    private WebClient webClient;

    @Autowired
    public ViaCepController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://viacep.com.br/").build();
    }
        @GetMapping("/consulta-cep/{cep}")
        public CepResponse getCep(@PathVariable Integer cep){
            return this.webClient.get()
                    .uri("/ws/{cep}/json", cep)
                    .retrieve()
                    .bodyToMono(CepResponse.class)
                    .block();
        }
}
