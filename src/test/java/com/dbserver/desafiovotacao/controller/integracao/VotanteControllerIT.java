package com.dbserver.desafiovotacao.controller.integracao;

import com.dbserver.desafiovotacao.dto.PautaRequest;
import com.dbserver.desafiovotacao.dto.VotanteRequest;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VotanteControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Teste para encontrar um associado no banco")
    public void testEncontrouVotanteIT() throws Exception {
        mockMvc.perform(get("/voto/3a38279d-f43d-485e-8160-4f7d0fd4cd1c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3a38279d-f43d-485e-8160-4f7d0fd4cd1c"))
                .andExpect(jsonPath("$.cpf").value("28789248964"))
                .andExpect(jsonPath("$.votoEnum").value("SIM"));
    }


    @Test
    @DisplayName("Teste para criar um novo associado no banco")
    public void testCriaVotanteIT() throws Exception {
        VotanteRequest votanteRequest = new VotanteRequest("24046283050", VotoEnum.SIM);
        String novoVotante = mapper.writeValueAsString(votanteRequest);
        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoVotante))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Teste para criar um associado invalido no banco")
    public void testCriaVotanteInvalidoIT() throws Exception {
        String novoVotante = mapper.writeValueAsString(null);
        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoVotante))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Teste para criar um associado com cpf invalido no banco")
    public void testCriaVotanteCPFInvalidoIT() throws Exception {
        VotanteRequest votanteRequest = new VotanteRequest("24046283058", VotoEnum.SIM);
        String novoVotante = mapper.writeValueAsString(votanteRequest);
        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoVotante))
                .andExpect(status().isMethodNotAllowed());
    }
}
