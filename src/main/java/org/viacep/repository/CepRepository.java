package org.viacep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.viacep.entity.dto.UfDto;
import org.viacep.entity.dto.response.LogResponse;

import java.util.List;

@Repository
public interface CepRepository extends JpaRepository <UfDto, String> {

   @Query("SELECT new org.viacep.entity.dto.response.LogResponse(l.cep, l.dtHrConsulta) " +
           "FROM UfDto l WHERE UPPER(l.uf) = UPPER(:uf) " +
           "ORDER BY l.dtHrConsulta DESC")
    List<LogResponse> findListCep(@Param("uf") String uf);

}
