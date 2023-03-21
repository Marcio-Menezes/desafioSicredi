/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.VotanteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar Votante Service")
public class VotanteServiceImplementacaoIT {

    @Mock
    private VotanteRepositorio votanteRepositorio;

    @InjectMocks
    VotanteServiceImplementacao votanteService;

    String id = "f5425a38-9fbd-43ac-b810-7ef31fd0f1dd";

    Votante votante;

    @BeforeEach
    public void setUp() {
        votante = Votante.builder().id(UUID.fromString(id)).voto(VotoEnum.NAO).build();
    }

    @Test
    public void testFindByIdSucesso() {
        when(votanteRepositorio.findById(votante.getId())).thenReturn(Optional.of(votante));
        Optional<Votante> retorno = votanteService.findById(votante.getId());
        assertEquals(Optional.of(votante), retorno);
    }

    @Test
    public void testFindByIdFalho() {
        UUID idRandom = UUID.randomUUID();
        when(votanteRepositorio.findById(idRandom)).thenReturn(Optional.of(votante));
        Optional<Votante> retorno = votanteService.findById(votante.getId());
        assertEquals(Optional.empty(), retorno);
    }

    @Test
    public void testFindAll() {
        List<Votante> listaAssociados = new ArrayList<>();
        listaAssociados.add(votante);
        when(votanteRepositorio.findAll()).thenReturn(listaAssociados);
        Iterable<Votante> resultado = votanteService.findAll();
        assertEquals(listaAssociados, resultado);
    }

    @Test
    public void testSalvarVotante() {
        when(votanteRepositorio.save(votante)).thenReturn(votante);
		votanteService.salvarVotante(votante);
		verify(votanteRepositorio, times(1)).save(votante);
    }

}
