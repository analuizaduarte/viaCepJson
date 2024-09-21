package org.viacep.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CepResponse {
    private String cep;
    @JsonAlias("logradouro")
    private String rua;
    private String bairro;
    @JsonAlias("localidade")
    private String cidade;
    private String uf;
}
