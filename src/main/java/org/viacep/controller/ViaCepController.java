package org.viacep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.viacep.entity.dto.response.CepResponse;
import org.viacep.entity.dto.response.LogResponse;
import org.viacep.repository.CepRepository;
import org.viacep.service.CepService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ViaCepController {

    private final CepService cepService;

    @Autowired
    private CepRepository cepRepository;

        @GetMapping("/consulta-cep/{cep}")
        public CepResponse getCep(@PathVariable String cep){
            return cepService.getCep(cep);
        }

        @GetMapping("/lista-ceps")
        public List<LogResponse> getListaCeps(@RequestParam(value = "uf", required = true) String uf) {
           return cepService.getListaCeps(uf);
        }
}
