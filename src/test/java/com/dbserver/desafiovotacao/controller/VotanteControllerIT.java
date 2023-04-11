
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.dto.VotanteRequest;
import com.dbserver.desafiovotacao.dto.VotanteResponse;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.exception.FalhaBuscaException;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.implementacao.VotanteServiceImplementacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar VotanteController")
public class VotanteControllerIT {
    
    @Autowired
    MockMvc mockito;

    @MockBean
    VotanteServiceImplementacao votanteService;
    Votante votante;
    VotanteResponse votanteResponse;

    @BeforeEach
    public void setUp() {
        votante = Votante.builder().id(UUID.randomUUID()).idVotante("codTeste").voto(VotoEnum.NAO).build();
        votanteResponse = new VotanteResponse(votante);
    }

    @Test
    @DisplayName("Teste de Criar um associado valido")
    public void testCriarAssociadoValido() throws Exception {
        VotanteRequest votanteRequest = new VotanteRequest("codTeste", VotoEnum.NAO);
        given(votanteService.salvarVotante(votanteRequest)).willReturn(votante);

        ObjectMapper mapper = new ObjectMapper();
        String novoVotante = mapper.writeValueAsString(votanteResponse);
        this.mockito.perform(post("/voto")
                        .content(novoVotante)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        verify(votanteService, times(1)).salvarVotante(votanteRequest);
    }

    @Test
    @DisplayName("Teste de Criar um associado invalido")
    public void testCriarAssociadoInvalido() throws Exception {
        VotanteRequest novoVotanteRequest = new VotanteRequest("", VotoEnum.SIM);
        Votante novoVotante = Votante.builder().idVotante(novoVotanteRequest.codAssociado()).voto(novoVotanteRequest.votoEnum()).build();
        given(votanteService.salvarVotante(novoVotanteRequest)).willReturn(novoVotante);
        ObjectMapper mapper = new ObjectMapper();
        String falha = mapper.writeValueAsString(novoVotanteRequest);
        this.mockito.perform(post("/voto")
                        .content(falha)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        verify(votanteService, times(0)).salvarVotante(novoVotanteRequest);
    }
    
    @Test
    @DisplayName("Teste de procurar um votante existente")
    public void testaProcuraVotanteExistente() throws Exception{
        VotanteResponse votanteResponse = new VotanteResponse(votante);
        given(votanteService.encontrarVotantePorID(votante.getId())).willReturn(Optional.of(votante));

		ObjectMapper mapper = new ObjectMapper();
		String votanteComoJSON = mapper.writeValueAsString(votanteResponse);

		mockito.perform(get("/voto/" + votante.getId()).content(votanteComoJSON)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(content().json(votanteComoJSON));
        verify(votanteService, times(1)).encontrarVotantePorID(votante.getId());
    }
    
    @Test
    @DisplayName("Teste de procurar um votante invalido")
    public void testaProcuraVotanteInvalido() throws Exception{
        UUID idInvalido = UUID.randomUUID();
        given(votanteService.encontrarVotantePorID(idInvalido)).willThrow(FalhaBuscaException.class);

		mockito.perform(get("/voto/" + idInvalido)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
        verify(votanteService, times(1)).encontrarVotantePorID(idInvalido);
    }
    @Test
    @DisplayName("Teste para retornar o total de votantes")
    public void testTotalVotantes() throws Exception{
        long resultado = 1;
        given(votanteService.totalVotantes()).willReturn(resultado);
        this.mockito.perform(get("/voto/total")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        verify(votanteService, times(1)).totalVotantes();
    }
    
}
