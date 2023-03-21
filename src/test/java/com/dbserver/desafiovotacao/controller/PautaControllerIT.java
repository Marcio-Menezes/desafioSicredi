
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.PautaServiceImplementacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testando o PautaController")
public class PautaControllerIT {
    
    @Autowired
    MockMvc mockito;

    @MockBean
    private PautaServiceImplementacao pautaService;
    
    Pauta pauta = new Pauta();
    Votante votante = new Votante();
    Votante votanteAutor = new Votante();
    
    @BeforeEach
    public void setUp() {
        votante.setId(UUID.randomUUID());
        votante.setVoto(VotoEnum.SIM);
        
        votanteAutor.setId(UUID.randomUUID());
        votanteAutor.setVoto(VotoEnum.SIM);
        
        pauta.setId(UUID.randomUUID());
        pauta.setTitulo("Teste");
        pauta.setDescricao("Esse é um teste unitário");
        pauta.setAutorPauta(votanteAutor);
        pauta.setHash("2h-23bh5");
        pauta.setAssociados(new ArrayList<>());
        pauta.adicionarAssociado(votante);
        pauta.adicionarAssociado(votanteAutor);
    }
    @Test
    @DisplayName("Teste de Criar uma pauta")
    public void testCriarPauta() throws Exception {
        given(pautaService.salvarPauta(pauta)).willReturn(pauta);

        ObjectMapper mapper = new ObjectMapper();
        String novaPauta = mapper.writeValueAsString(pauta);
        this.mockito.perform(post("/pauta")
                .content(novaPauta)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("Teste verificar o total de votantes em uma pauta")
    public void testTotalVotantes() throws Exception {
        Integer result = pauta.getAssociados().size();
        when(pautaService.totalVotantes(pauta.getId())).thenReturn(result);
        assertEquals(result, pautaService.totalVotantes(pauta.getId()));
    }

    @Test
    @DisplayName("Teste de procurar uma pauta por id")
    public void testEncontrarPautaPorId() throws Exception {
        given(pautaService.encontrarPautaPorID(pauta.getId())).willReturn(Optional.of(pauta));

        ObjectMapper mapper = new ObjectMapper();
        String encontrarPautaJSON = mapper.writeValueAsString(pauta);
        this.mockito.perform(get("/pauta/" + pauta.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(encontrarPautaJSON))
                .andExpect(status().isOk());
    }
    
}