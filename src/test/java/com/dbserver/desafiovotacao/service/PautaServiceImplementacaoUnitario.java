/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.controller.Inicializador;
import com.dbserver.desafiovotacao.dto.*;
import com.dbserver.desafiovotacao.enums.PautaAndamentoEnum;
import com.dbserver.desafiovotacao.enums.PautaResultadoEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.exception.AcaoInvalidaException;
import com.dbserver.desafiovotacao.exception.FalhaBuscaException;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.PautaRepositorio;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.dbserver.desafiovotacao.service.implementacao.PautaServiceImplementacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar Pauta Service")
public class PautaServiceImplementacaoUnitario {
    
    @Mock
    private PautaRepositorio pautaRepositorio;
    @Mock
    private VotanteService votanteService;
    @InjectMocks
    PautaServiceImplementacao pautaService;
    Pauta pauta;
    Votante votante;
    Votante votanteAutor;
    PautaRequest pautaRequest;
    
    @BeforeEach
    public void setUp() {
        Inicializador inicializador = new Inicializador();
        votanteAutor = inicializador.construirVotanteAutor();
        pauta = inicializador.construirPauta();
        pautaRequest = new PautaRequest("Teste", votanteAutor.getId(), "");

    }

    @Test
    @DisplayName("Testar encontrar pauta existente por ID")
    public void testEncontrarPautaPorIDSucesso() {
        given(pautaRepositorio.findById(pauta.getId())).willReturn(Optional.of(pauta));
		Optional<Pauta> resposta = pautaService.encontrarPautaPorID(pauta.getId());
		assertEquals(pauta, resposta.get());
    }
    
    @Test
    @DisplayName("Testar encontrar pauta inexistente por ID")
    public void testEncontrarPautaPorIDFalha() {
        UUID idRandom = UUID.randomUUID();
        given(pautaRepositorio.findById(idRandom)).willReturn(Optional.empty());
        assertThrows(FalhaBuscaException.class, () -> {
            pautaService.encontrarPautaPorID(idRandom);
        });
    }

    @Test
    @DisplayName("Testar encontrar pauta existente por hash")
    public void testEncontrarPautaPorHashSucesso() {
        given(pautaRepositorio.findByHash(pauta.getHash())).willReturn(Optional.of(pauta));
		Optional<Pauta> resposta = pautaRepositorio.findByHash(pauta.getHash());
		assertEquals(pauta, resposta.get());
    }
    
    @Test
    @DisplayName("Testar encontrar pauta inexistente por hash")
    public void testEncontrarPautaPorHashFalha() {
        
        String hashTeste = "TesteDeHash";
        given(pautaRepositorio.findByHash(hashTeste)).willReturn(Optional.empty());
		assertThrows(FalhaBuscaException.class, () -> pautaService.encontrarPautaPorHash(hashTeste));
    }

    @Test
    @DisplayName("Testar o salvamento de uma pauta")
    public void testSalvarPauta() {
        VotanteService votanteService = Mockito.mock(VotanteService.class);
        when(votanteService.encontrarVotantePorID(pautaRequest.idAutor())).thenReturn(Optional.of(votanteAutor));
        pautaService = new PautaServiceImplementacao(votanteService, pautaRepositorio);
        Pauta novaPauta = pautaService.salvarPauta(pautaRequest);
        when(pautaRepositorio.save(novaPauta)).thenReturn(novaPauta);
        pautaRepositorio.save(novaPauta);
		verify(pautaRepositorio, times(1)).save(novaPauta);
    }

    @Test
    @DisplayName("Teste de mostrar todas as pautas")
    public void testaMostrarPautas(){
        Pageable pageable = Pageable.ofSize(10);
        List<Pauta> listaPautas = Arrays.asList(pauta);
        Page<Pauta> pautaPage = new PageImpl<>(listaPautas);
        given(pautaService.mostraPautas(pageable)).willReturn(pautaPage);
        assertEquals(1,pautaPage.getTotalElements());
        assertEquals(pauta,listaPautas.get(0));

    }

    @Test
    @DisplayName("Teste para retornar o total de votantes em uma pauta")
    public void testTotalVotantes(){
        given(pautaRepositorio.findById(pauta.getId())).willReturn(Optional.of(pauta));
        Integer result = pautaService.totalVotantes(pauta.getId());
        assertEquals(1,result);
    }

    @Test
    @DisplayName("Teste para retornar o total de votantes de uma pauta vazia")
    public void testTotalVotantesVazio(){
        UUID idInvalido = UUID.randomUUID();
        given(pautaRepositorio.findById(idInvalido)).willReturn(Optional.empty());
        Integer result = pautaService.totalVotantes(idInvalido);
        assertEquals(0,result);
    }

    @Test
    @DisplayName("Teste de adicionar uma novo votante a pauta")
    public void testaAdicionarAssociado() {
        Votante novoVotante = Votante.builder().id(UUID.randomUUID()).cpf("testeVot").voto(VotoEnum.SIM).build();
        ClienteRequest clienteRequest  = new ClienteRequest(novoVotante.getId());
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor).hash("378763").associados(new ArrayList<>()).build();
        given(pautaRepositorio.findByHash(novaPauta.getHash())).willReturn(Optional.of(novaPauta));
        given(votanteService.encontrarVotantePorID(clienteRequest.id())).willReturn(Optional.of(novoVotante));
        pautaService.adicionarAssociado(novaPauta.getHash(),clienteRequest);
        assertEquals(1,novaPauta.getAssociados().size());
        assertEquals(novoVotante,novaPauta.getAssociados().get(0));
        verify(pautaRepositorio, times(1)).save(novaPauta);

    }

    @Test
    @DisplayName("Teste de adicionar um votante nulo a uma pauta")
    public void testaAdicionarAssociadoFalho(){
        UUID idInvalido = UUID.randomUUID();
        ClienteRequest clienteRequest  = new ClienteRequest(idInvalido);
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor).hash("378763").associados(new ArrayList<>()).build();

        given(pautaRepositorio.findByHash(novaPauta.getHash())).willReturn(Optional.of(novaPauta));
        given(votanteService.encontrarVotantePorID(clienteRequest.id())).willReturn(Optional.empty());
        assertEquals(0,novaPauta.getAssociados().size());
        assertTrue(novaPauta.getAssociados().isEmpty());
        verify(pautaRepositorio, times(0)).save(novaPauta);

    }

    @Test
    @DisplayName("Teste de finalizar uma Pauta aprovada com sucesso")
    public void testaFinalizaPautaAprovada(){
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor)
                .aberturaPauta(LocalTime.now().minusMinutes(2)).fechamentoPauta(LocalTime.now()).hash("378763").associados(new ArrayList<>()).build();
        novaPauta.getAssociados().add(new Votante(UUID.randomUUID(), "votante1", VotoEnum.SIM));
        novaPauta.getAssociados().add(new Votante(UUID.randomUUID(), "votante2", VotoEnum.SIM));
        novaPauta.getAssociados().add(new Votante(UUID.randomUUID(), "votante3", VotoEnum.NAO));
        given(pautaRepositorio.findByHash(novaPauta.getHash())).willReturn(Optional.of(novaPauta));
        pautaService.finalizarPauta(novaPauta.getHash());
        assertEquals(PautaResultadoEnum.APROVADO, novaPauta.getResultado());
        assertEquals(PautaAndamentoEnum.CONCLUIDO, novaPauta.getAndamento());
    }

    @Test
    @DisplayName("Teste de finalizar uma Pauta indeferida")
    public void testaFinalizaPautaIndeferida(){
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor)
                .aberturaPauta(LocalTime.now().minusSeconds(61)).fechamentoPauta(LocalTime.now()).hash("378763").associados(new ArrayList<>()).build();
        novaPauta.getAssociados().add(new Votante(UUID.randomUUID(), "votante1", VotoEnum.SIM));
        novaPauta.getAssociados().add(new Votante(UUID.randomUUID(), "votante2", VotoEnum.NAO));
        novaPauta.getAssociados().add(new Votante(UUID.randomUUID(), "votante3", VotoEnum.NAO));
        given(pautaRepositorio.findByHash(novaPauta.getHash())).willReturn(Optional.of(novaPauta));
        pautaService.finalizarPauta(novaPauta.getHash());
        assertTrue(novaPauta.getResultado().equals(PautaResultadoEnum.INDEFERIDO));
        assertTrue(novaPauta.getAndamento().equals(PautaAndamentoEnum.CONCLUIDO));
    }

    @Test
    @DisplayName("Teste de finalizar uma Pauta sem votos")
    public void testaFinalizaPautaSemVotos(){
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor)
                .aberturaPauta(LocalTime.now().minusSeconds(61)).fechamentoPauta(LocalTime.now()).hash("378763").associados(new ArrayList<>()).build();
        given(pautaRepositorio.findByHash(novaPauta.getHash())).willReturn(Optional.of(novaPauta));
        assertThrows(AcaoInvalidaException.class, () -> pautaService.finalizarPauta(novaPauta.getHash()));
    }

    @Test
    @DisplayName("Teste de finalizar uma Pauta invalida")
    public void testaFinalizaPautaInvalida(){
        String hashInvalido = "Invalido";
        given(pautaRepositorio.findByHash(hashInvalido)).willThrow(FalhaBuscaException.class);
        assertThrows(FalhaBuscaException.class, () -> {
            pautaService.finalizarPauta(hashInvalido);
        });
    }

    
}
