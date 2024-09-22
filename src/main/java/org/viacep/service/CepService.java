package org.viacep.service;

import org.viacep.entity.dto.response.CepResponse;
import org.viacep.entity.dto.response.LogResponse;

import java.util.List;

public interface CepService {
    CepResponse getCep(String cep);

    List<LogResponse> getListaCeps(String uf);
}
