/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.controller.Inicializador;
import com.dbserver.desafiovotacao.dto.VotanteRequest;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.VotanteRepositorio;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dbserver.desafiovotacao.service.implementacao.VotanteServiceImplementacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar Votante Service")
public class VotanteServiceImplementacaoUnitario {

    @Mock
    private VotanteRepositorio votanteRepositorio;

    @InjectMocks
    VotanteServiceImplementacao votanteService;

    Votante votante;


    @BeforeEach
    public void setUp() {
        Inicializador inicializador = new Inicializador();
        votante = inicializador.construirVotante();
    }

    @Test
    public void testFindByIdSucesso() {
        when(votanteRepositorio.findById(votante.getId())).thenReturn(Optional.of(votante));
        Optional<Votante> retorno = votanteService.encontrarVotantePorID(votante.getId());
        assertEquals(Optional.of(votante), retorno);
    }

    @Test
    public void testFindByIdFalho() {
        UUID idRandom = UUID.randomUUID();
        when(votanteRepositorio.findById(idRandom)).thenReturn(Optional.of(votante));
        Optional<Votante> retorno = votanteService.encontrarVotantePorID(votante.getId());
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
        VotanteRequest votanteRequest = new VotanteRequest("07385928030", VotoEnum.NAO);
        Votante novoVotante = Votante.builder().cpf("07385928030").voto(VotoEnum.NAO).build();
        when(votanteRepositorio.save(novoVotante)).thenReturn(novoVotante);
		votanteService.salvarVotante(votanteRequest);
		verify(votanteRepositorio, times(1)).save(novoVotante);
    }

    @Test
    @DisplayName("Teste de mostrar o total de votantes")
    public void testaTotalVotantes() {
        List<Votante> listaVotantes = Arrays.asList(votante);
        given(votanteService.totalVotantes()).willReturn((long) listaVotantes.size());
        assertEquals(1,listaVotantes.size());
        assertEquals(votante,listaVotantes.get(0));
    }

}
