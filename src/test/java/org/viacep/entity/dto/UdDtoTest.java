package org.viacep.entity.dto;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UdDtoTest {
    @Test
    public void testGettersAndSetters() {
        UfDto ufDto = new UfDto();

        ufDto.setId(1);
        ufDto.setUf("MG");
        ufDto.setCep("37550-110");
        ufDto.setDtHrConsulta(LocalDate.now());

        assertEquals("MG", ufDto.getUf());
        assertEquals("37550-110", ufDto.getCep());
        assertEquals(LocalDate.now(), ufDto.getDtHrConsulta());
    }
}
