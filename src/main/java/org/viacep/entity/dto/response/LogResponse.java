package org.viacep.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LogResponse {

    private String cep;

    @JsonAlias("dt_hr_consulta")
    private Date dtHrConsulta;
}
