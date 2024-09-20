package org.viacep.entity.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CepResponse {
    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
}
