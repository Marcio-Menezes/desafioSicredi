
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.enums.AssembleiaEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.AssembleiaService;
import com.dbserver.desafiovotacao.service.AssembleiaServiceImplementacao;
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
@DisplayName("Testar AssembleiaController")
public class AssembleiaControllerIT {
    
    @Autowired
    MockMvc mockito;

    @MockBean
    private AssembleiaServiceImplementacao assembleiaService;
    
    Assembleia assembleia = new Assembleia();
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
        
        assembleia.setId(UUID.randomUUID());
        assembleia.setListaPauta(new ArrayList<>());
        assembleia.setStatus(AssembleiaEnum.MOVIMENTO);
        assembleia.adicionarPauta(pauta);
    }
    
    @Test
    @DisplayName("Teste de Criar uma assembleia")
    public void testCriarAssembleia() throws Exception {

        given(assembleiaService.salvarAssembleia(assembleia)).willReturn(assembleia);

        ObjectMapper mapper = new ObjectMapper();
        String novaAssembleia = mapper.writeValueAsString(assembleia);
        this.mockito.perform(post("/api")
                .content(novaAssembleia)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Teste de procurar uma assembleia existente")
    public void testProcuraAssembleia() throws Exception {
        given(assembleiaService.encontrarAssembleiaPorID(assembleia.getId())).willReturn(Optional.of(assembleia));

        ObjectMapper mapper = new ObjectMapper();
        String encontrarAssembleiaJSON = mapper.writeValueAsString(assembleia);
        this.mockito.perform(get("/api/" + assembleia.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(encontrarAssembleiaJSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste para retornar o total de pautas de uma assembleia")
    public void testTotalPautas() {
        Integer result = assembleia.getListaPauta().size();
        when(assembleiaService.totalPautas(assembleia.getId())).thenReturn(result);
        assertEquals(result, assembleiaService.totalPautas(assembleia.getId()));

    }
    
}
