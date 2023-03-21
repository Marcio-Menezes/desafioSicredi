
package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.service.VotanteService;
import com.dbserver.desafiovotacao.service.VotanteServiceImplementacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar VotanteController")
public class VotanteControllerIT {
    
    @Autowired
    MockMvc mockMvc;

    @MockBean
    VotanteServiceImplementacao votanteService;
    
    String id = "f5425a38-9fbd-43ac-b810-7ef31fd0f1dd";
    
    
    Votante votante = Votante.builder().id(UUID.fromString(id)).voto(VotoEnum.NAO).build();
    
    @Test
    @DisplayName("Teste de procurar um votante existente")
    public void testaProcuraVotanteExistente() throws Exception{
        
        given(votanteService.findById(votante.getId())).willReturn(Optional.of(votante));

		ObjectMapper mapper = new ObjectMapper();
		String votanteComoJSON = mapper.writeValueAsString(votante);

		mockMvc.perform(get("/votante/" + votante.getId()).content(votanteComoJSON)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(content().json(votanteComoJSON));
    }
    
    @Test
    @DisplayName("Teste de procurar um votante invalido")
    public void testaProcuraVotanteInvalido() throws Exception{
        UUID idInvalido = UUID.randomUUID();
        given(votanteService.findById(idInvalido)).willReturn(Optional.of(votante));

		mockMvc.perform(get("/votante/" + idInvalido)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
    }
    @Test
    @DisplayName("Teste para retornar o total de votantes")
    public void testTotalVotantes() throws Exception{
        Integer result = 1;
        when(votanteService.totalVotantes()).thenReturn(result);
        assertEquals(result, votanteService.totalVotantes());

    }
    
}
