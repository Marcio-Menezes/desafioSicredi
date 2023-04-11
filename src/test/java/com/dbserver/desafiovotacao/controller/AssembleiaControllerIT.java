
package com.dbserver.desafiovotacao.controller;


import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.AssembleiaResponse;
import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.enums.PautaAndamentoEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.exception.FalhaBuscaException;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.implementacao.AssembleiaServiceImplementacao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar AssembleiaController")
public class AssembleiaControllerIT {
    
    @Autowired
    MockMvc mockito;

    @Autowired private final ObjectMapper mapper = new ObjectMapper();
    private String retornoJSON;

    @MockBean
    private AssembleiaServiceImplementacao assembleiaService;

    AssembleiaRequest assembleiaRequest = new AssembleiaRequest("teste unitario");
    Assembleia assembleia = new Assembleia();

    Pauta pauta = new Pauta();
    Votante votante = new Votante();
    Votante votanteAutor = new Votante();

    AssembleiaResponse assembleiaResponse;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        votante = Votante.builder().id(UUID.randomUUID()).idVotante("votante1").voto(VotoEnum.SIM).build();
        votanteAutor = Votante.builder().id(UUID.randomUUID()).idVotante("autoria1").voto(VotoEnum.AUTORIA).build();
        pauta = Pauta.builder().id(UUID.randomUUID()).titulo("Teste").descricao("Esse é um teste unitário").associados(new ArrayList<>()).autorPauta(votanteAutor).hash("2h-23bh5").build();
        pauta.getAssociados().add(votante);
        assembleia = Assembleia.builder().id(UUID.randomUUID()).nomeAssembleia("Teste de Assembleia").aberturaAssembleia(LocalDateTime.now()).listaPauta(new ArrayList<>()).nomeAssembleia("Teste Assembleia").build();
        assembleia.getListaPauta().add(pauta);
        assembleiaResponse = new AssembleiaResponse(assembleia);
        retornoJSON = mapper.writeValueAsString(assembleiaResponse);
    }
    
    @Test
    @DisplayName("Teste de Criar uma assembleia")
    public void testCriarAssembleia() throws Exception {
        given(assembleiaService.salvarAssembleia(assembleiaRequest)).willReturn(assembleia);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String novaAssembleia = mapper.writeValueAsString(assembleiaRequest);
        this.mockito.perform(post("/api")
                .content(novaAssembleia)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        verify(assembleiaService, times(1)).salvarAssembleia(assembleiaRequest);
    }

    @Test
    @DisplayName("Teste de falhar ao criar uma assembleia")
    public void testCriarAssembleiaInvalida() throws Exception {
        AssembleiaRequest novaAssembleiaRequest = new AssembleiaRequest("");
        Assembleia novaAssembleia = Assembleia.builder().nomeAssembleia(novaAssembleiaRequest.nomeAssembleia()).build();
        given(assembleiaService.salvarAssembleia(novaAssembleiaRequest)).willReturn(novaAssembleia);

        ObjectMapper mapper = new ObjectMapper();
        String falha = mapper.writeValueAsString(novaAssembleiaRequest);
        this.mockito.perform(post("/api")
                        .content(falha)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        verify(assembleiaService, times(0)).salvarAssembleia(novaAssembleiaRequest);
    }

    @Test
    @DisplayName("Teste de adicionar uma nova pauta a assembleia")
    public void testaAdicionarPauta() throws Exception {
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor).hash("378763").build();
        ClienteRequest clienteRequest  = new ClienteRequest(novaPauta.getId());
        given(assembleiaService.adicionarPauta(assembleia.getId(),clienteRequest)).willReturn(assembleia);
        ObjectMapper mapper = new ObjectMapper();
        String encontrarAssembleiaJSON = mapper.writeValueAsString(clienteRequest);
        this.mockito.perform(post("/api/adicionapauta/" + assembleia.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(encontrarAssembleiaJSON))
                        .andExpect(status().isOk());
        verify(assembleiaService, times(1)).adicionarPauta(assembleia.getId(), clienteRequest);
    }

    @Test
    @DisplayName("Teste de finalizar uma Assembleia com Pauta em Movimento")
    public void testaFinalizaAssembleiaFalha() throws Exception {

        given(assembleiaService.finalizarAssembleia(assembleia.getId())).willReturn(assembleia);
        ObjectMapper mapper = new ObjectMapper();
        String encontrarAssembleiaJSON = mapper.writeValueAsString(assembleiaRequest);
        this.mockito.perform(get("/api/finalizarassembleia/" + assembleia.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(encontrarAssembleiaJSON))
                .andExpect(status().isOk());
        verify(assembleiaService, times(1)).finalizarAssembleia(assembleia.getId());
    }

    @Test
    @DisplayName("Teste de finalizar uma Assembleia com Pauta finalizada")
    public void testaFinalizaAssembleiaSucesso() throws Exception {
        assembleia.getListaPauta().get(0).setAndamento(PautaAndamentoEnum.CONCLUIDO);
        given(assembleiaService.finalizarAssembleia(assembleia.getId())).willReturn(assembleia);
        ObjectMapper mapper = new ObjectMapper();
        String encontrarAssembleiaJSON = mapper.writeValueAsString(assembleiaRequest);
        this.mockito.perform(get("/api/finalizarassembleia/" + assembleia.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(encontrarAssembleiaJSON))
                .andExpect(status().isOk());
        verify(assembleiaService, times(1)).finalizarAssembleia(assembleia.getId());
    }

    @Test
    @DisplayName("Teste de mostrar pautas")
    public void testaMostrarPautas() throws Exception {

        given(assembleiaService.mostraPautas(assembleia.getId())).willReturn(assembleia.getListaPauta());
        ObjectMapper mapper = new ObjectMapper();
        String encontrarAssembleiaJSON = mapper.writeValueAsString(assembleiaRequest);
        this.mockito.perform(get("/api/mostrarpautas/" + assembleia.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(encontrarAssembleiaJSON))
                .andExpect(status().isOk());
        verify(assembleiaService, times(1)).mostraPautas(assembleia.getId());
    }

    @Test
    @DisplayName("Teste de todas as assembleias")
    public void testaMostrarTodasAsAssembleias() throws Exception {
        Pageable pageable = Pageable.ofSize(10);
        List<AssembleiaResponse> listaAssembleias = Arrays.asList(assembleiaResponse);
        Page<AssembleiaResponse> pageAssembleias = new PageImpl<>(listaAssembleias);
        given(assembleiaService.mostrarAssembleias(pageable)).willReturn(pageAssembleias);
        String encontrarAssembleiaJSON = mapper.writeValueAsString(pageAssembleias);
        this.mockito.perform(get("/api/mostrarassembleias")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(encontrarAssembleiaJSON))
                .andExpect(status().isOk());
        verify(assembleiaService).mostrarAssembleias(pageable);
    }

    @Test
    @DisplayName("Teste para exibir toda a assembleia")
    public void testaMostrarTudo() throws Exception {
        Pageable pageable = Pageable.ofSize(10);
        List<Assembleia> listaAssembleias = Arrays.asList(assembleia);
        Page<Assembleia> pageAssembleias = new PageImpl<>(listaAssembleias);
        given(assembleiaService.mostraTudo(pageable)).willReturn(pageAssembleias);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String encontrarAssembleiaJSON = mapper.writeValueAsString(pageAssembleias);
        this.mockito.perform(get("/api/tudo")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(encontrarAssembleiaJSON))
                .andExpect(status().isOk());
        verify(assembleiaService, times(1)).mostraTudo(pageable);
    }

    @Test
    @DisplayName("Teste de procurar uma assembleia existente")
    public void testProcuraAssembleia() throws Exception {

        given(assembleiaService.encontrarAssembleiaPorID(assembleia.getId())).willReturn(Optional.of(assembleia));
        this.mockito.perform(get("/api/" + assembleia.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(retornoJSON))
                .andExpect(status().isOk());
        verify(assembleiaService, times(1)).encontrarAssembleiaPorID(assembleia.getId());
    }

    @Test
    @DisplayName("Teste de procurar uma assembleia inexistente")
    public void testProcuraAssembleiaInexistente() throws Exception {
        UUID assembleiaNula = UUID.randomUUID();
        given(assembleiaService.encontrarAssembleiaPorID(assembleiaNula)).willThrow(FalhaBuscaException.class);
        this.mockito.perform(get("/api/" + assembleiaNula)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
        verify(assembleiaService).encontrarAssembleiaPorID(assembleiaNula);
    }

    @Test
    @DisplayName("Teste para retornar o total de pautas de uma assembleia")
    public void testTotalPautas() throws Exception {
        Integer result = assembleia.getListaPauta().size();
        given(assembleiaService.totalPautas(assembleia.getId())).willReturn(result);
        this.mockito.perform(get("/api/numeropautas/" + assembleia.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        verify(assembleiaService, times(1)).totalPautas(assembleia.getId());

    }

}
    

