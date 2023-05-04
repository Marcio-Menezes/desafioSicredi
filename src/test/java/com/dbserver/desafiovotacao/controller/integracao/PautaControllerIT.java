package com.dbserver.desafiovotacao.controller.integracao;

import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.PautaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PautaControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    public static final String insert = "/insert.sql";

    @Test
    @DisplayName("Teste para encontrar uma pauta no banco")
    public void testEncontrouPautaIT() throws Exception {
        mockMvc.perform(get("/pauta/5ca07fd1-4419-401a-8ceb-58b3f92c24de"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5ca07fd1-4419-401a-8ceb-58b3f92c24de"))
                .andExpect(jsonPath("$.titulo").value("Padronizacao de tecnologia"))
                .andExpect(jsonPath("$.resultadoEnum").value("APROVADO"));
    }

    @Test
    @DisplayName("Teste para encontrar uma pauta invalida no banco")
    public void testPautaNaoEncontradaIT() throws Exception {
        mockMvc.perform(get("/pauta/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insert)
    @DisplayName("Teste para criar uma pauta no banco")
    public void testCriaPautaIT() throws Exception {
            PautaRequest pautaRequest = new PautaRequest("Nova Pauta", UUID.fromString("510292a5-d4fe-4482-a7cd-4eb7023a9b1f"), "");
        String novaPauta= mapper.writeValueAsString(pautaRequest);
        mockMvc.perform(post("/pauta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaPauta))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Teste para criar uma pauta invalida no banco")
    public void testCriaPautaInvalidaIT() throws Exception {
        String novaPauta= mapper.writeValueAsString(null);
        mockMvc.perform(post("/pauta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaPauta))
                .andExpect(status().isBadRequest());
    }


}
