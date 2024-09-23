package org.viacep.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "log")
@Getter
@Setter
public class UfDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String uf;

    @Column
    private String cep;

    @Column(name = "dt_hr_consulta")
    private LocalDate dtHrConsulta;
}
