
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.AssembleiaResponse;
import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.enums.AssembleiaEnum;
import com.dbserver.desafiovotacao.enums.PautaAndamentoEnum;
import com.dbserver.desafiovotacao.enums.PautaResultadoEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.exception.AcaoInvalidaException;
import com.dbserver.desafiovotacao.exception.FalhaBuscaException;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.AssembleiaRepositorio;

import java.time.LocalDateTime;
import java.util.*;

import com.dbserver.desafiovotacao.service.implementacao.AssembleiaServiceImplementacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar Assembleia Service")
public class AssembleiaServiceImplementacaoIT {
    
    @Mock
    private AssembleiaRepositorio assembleiaRepositorio;
    @Mock
    private PautaService pautaService;
    @InjectMocks
    private AssembleiaServiceImplementacao assembleiaService;
    Assembleia assembleia;
    Pauta pauta;
    Votante votante;
    Votante votanteAutor;

    @BeforeEach
    public void setUp() {

        votante = Votante.builder().id(UUID.randomUUID()).idVotante("votante1").voto(VotoEnum.SIM).build();
        votanteAutor = Votante.builder().id(UUID.randomUUID()).idVotante("autoria1").voto(VotoEnum.AUTORIA).build();
        pauta = Pauta.builder().id(UUID.randomUUID()).titulo("Teste").descricao("Esse é um teste unitário").autorPauta(votanteAutor).associados(new ArrayList<>()).hash("2h-23bh5").build();
        pauta.getAssociados().add(votante);
        assembleia = Assembleia.builder().id(UUID.randomUUID()).nomeAssembleia("Teste de Assembleia").status(AssembleiaEnum.MOVIMENTO).listaPauta(new ArrayList<>()).build();
    }


    @Test
    @DisplayName("Teste para retornar uma assembleia valida")
    public void testEncontrarAssembleiaPorIDSucesso() {
        given(assembleiaRepositorio.findById(assembleia.getId())).willReturn(Optional.of(assembleia));
        Optional<Assembleia> resposta = assembleiaService.encontrarAssembleiaPorID(assembleia.getId());
		assertEquals(assembleia, resposta.get());
    }
    
    @Test
    @DisplayName("Teste para retornar uma assembleia invalida")
    public void testEncontrarAssembleiaPorIDFalha() {
        UUID idRandom = UUID.randomUUID();
        given(assembleiaRepositorio.findById(idRandom)).willReturn(Optional.empty());
		assertThrows(FalhaBuscaException.class, () -> assembleiaService.encontrarAssembleiaPorID(idRandom));
    }

    @Test
    @DisplayName("Teste para salvar uma assembleia")
    public void testSalvarAssembleia() {
        AssembleiaRequest novaAssembleiaRequest = new AssembleiaRequest("Teste Unitário");
        Assembleia novaAssembleia = Assembleia.builder().nomeAssembleia(novaAssembleiaRequest.nomeAssembleia())
                .aberturaAssembleia(LocalDateTime.now())
                .status(AssembleiaEnum.MOVIMENTO)
                .listaPauta(new ArrayList<>()).build();
        given(assembleiaService.salvarAssembleia(novaAssembleiaRequest)).willReturn(novaAssembleia);
        assembleiaService.salvarAssembleia(novaAssembleiaRequest);
		verify(assembleiaRepositorio, times(1)).save(novaAssembleia);
    }

    @Test
    @DisplayName("Teste para retornar o total de pautas de uma assembleia")
    public void testTotalPautas(){
        given(assembleiaRepositorio.findById(assembleia.getId())).willReturn(Optional.of(assembleia));
        assembleia.getListaPauta().add(pauta);
        Integer result = assembleiaService.totalPautas(assembleia.getId());
        assertEquals(1,result);
    }
    @Test
    @DisplayName("Teste para retornar o total de pautas de uma assembleia inexistente")
    public void testTotalPautasDeAssembleiaNula(){
        UUID idInvalido = UUID.randomUUID();
        given(assembleiaRepositorio.findById(idInvalido)).willReturn(Optional.empty());
        assertThrows(FalhaBuscaException.class, () -> assembleiaService.totalPautas(idInvalido));
    }

    @Test
    @DisplayName("Teste para mostrar tudo")
    public void testaMostrarTudo(){
        List<Assembleia> listaAssembleias = Arrays.asList(assembleia);
        Page<Assembleia> assembleiaPage = new PageImpl<>(listaAssembleias);
        Pageable pageable = Pageable.ofSize(10);
        given(assembleiaRepositorio.findAll(pageable)).willReturn(assembleiaPage);
        Page<Assembleia> resposta = assembleiaService.mostraTudo(pageable);
        assertEquals(assembleiaPage, resposta);
    }

    @Test
    @DisplayName("Teste de mostrar todas as pautas")
    public void testaMostrarPautas(){
        given(assembleiaRepositorio.findById(assembleia.getId())).willReturn(Optional.of(assembleia));
        List<Pauta> listaPauta = assembleia.getListaPauta();
        Iterable<Pauta> resposta = assembleiaService.mostraPautas(assembleia.getId());
        assertEquals(listaPauta, resposta);

    }

    @Test
    @DisplayName("Teste de adicionar uma nova pauta a assembleia")
    public void testaAdicionarPauta() {
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor).hash("378763").build();
        ClienteRequest clienteRequest  = new ClienteRequest(novaPauta.getId());
        Assembleia novaAssembleia = Assembleia.builder().id(UUID.randomUUID()).listaPauta(new ArrayList<>()).build();
        given(assembleiaRepositorio.findById(novaAssembleia.getId())).willReturn(Optional.of(novaAssembleia));
        given(pautaService.encontrarPautaPorID(clienteRequest.id())).willReturn(Optional.of(novaPauta));
        assembleiaService.adicionarPauta(novaAssembleia.getId(),clienteRequest);
        assertEquals(1,novaAssembleia.getListaPauta().size());
        assertEquals(novaPauta,novaAssembleia.getListaPauta().get(0));
        verify(assembleiaRepositorio, times(1)).save(novaAssembleia);

    }

    @Test
    @DisplayName("Teste de adicionar uma nova pauta nula")
    public void testaAdicionarPautaFalha() {
        UUID idInvalido = UUID.randomUUID();
        ClienteRequest clienteRequest  = new ClienteRequest(idInvalido);
        Assembleia novaAssembleia = Assembleia.builder().id(UUID.randomUUID()).listaPauta(new ArrayList<>()).build();
        given(assembleiaRepositorio.findById(novaAssembleia.getId())).willReturn(Optional.of(novaAssembleia));
        given(pautaService.encontrarPautaPorID(clienteRequest.id())).willThrow(FalhaBuscaException.class);
        assertThrows(FalhaBuscaException.class, () -> assembleiaService.adicionarPauta(novaAssembleia.getId(),clienteRequest));
        verify(assembleiaRepositorio, times(0)).save(novaAssembleia);

    }

    @Test
    @DisplayName("Teste de todas as assembleias")
    public void testaMostrarTodasAsAssembleias() {
        Pageable pageable = Pageable.ofSize(10);
        AssembleiaResponse assembleiaResponse = new AssembleiaResponse(assembleia);
        List<Assembleia> lista = Arrays.asList(assembleia);
        Page<Assembleia> assembleiaPage = new PageImpl<>(lista);
        given(assembleiaRepositorio.findAll(pageable)).willReturn(assembleiaPage);
        Page<AssembleiaResponse> resultado = assembleiaService.mostrarAssembleias(pageable);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(assembleiaResponse, resultado.getContent().get(0));
    }

    @Test
    @DisplayName("Teste de finalizar uma Assembleia com Pauta finalizada")
    public void testaFinalizaAssembleiaSucesso(){
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor).hash("378763").resultado(PautaResultadoEnum.APROVADO).andamento(PautaAndamentoEnum.CONCLUIDO).build();
        Assembleia novaAssembleia = Assembleia.builder().id(UUID.randomUUID()).nomeAssembleia("Nova Assembleia")
                .aberturaAssembleia(LocalDateTime.now()).listaPauta(new ArrayList<>()).status(AssembleiaEnum.MOVIMENTO).build();
        novaAssembleia.getListaPauta().add(novaPauta);
        given(assembleiaRepositorio.findById(novaAssembleia.getId())).willReturn(Optional.of(novaAssembleia));
        assembleiaService.finalizarAssembleia(novaAssembleia.getId());
        assertTrue(novaAssembleia.getStatus().equals(AssembleiaEnum.FINALIZADO));
    }

    @Test
    @DisplayName("Teste de finalizar uma Assembleia com Pauta em andamento")
    public void testaFinalizaAssembleiaFalha(){
        Pauta novaPauta = Pauta.builder().id(UUID.randomUUID()).titulo("Nova Pauta").autorPauta(votanteAutor).hash("378763").andamento(PautaAndamentoEnum.APURANDO).build();
        Assembleia novaAssembleia = Assembleia.builder().id(UUID.randomUUID()).nomeAssembleia("Nova Assembleia")
                .aberturaAssembleia(LocalDateTime.now()).listaPauta(new ArrayList<>()).status(AssembleiaEnum.MOVIMENTO).build();
        novaAssembleia.getListaPauta().add(novaPauta);
        given(assembleiaRepositorio.findById(novaAssembleia.getId())).willReturn(Optional.of(novaAssembleia));
        assertThrows(AcaoInvalidaException.class, () -> assembleiaService.finalizarAssembleia(novaAssembleia.getId()));
    }

    @Test
    @DisplayName("Teste de finalizar uma Assembleia Inexistente")
    public void testaFinalizaAssembleiaInexistente(){
        UUID idInvalido = UUID.randomUUID();
        given(assembleiaRepositorio.findById(idInvalido)).willThrow(FalhaBuscaException.class);
        assertThrows(FalhaBuscaException.class, () -> assembleiaService.finalizarAssembleia(idInvalido));
    }
    
}
