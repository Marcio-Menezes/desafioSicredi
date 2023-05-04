package com.dbserver.desafiovotacao.controller.integracao;

import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.ClienteRequest;
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
public class AssembleiaControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Teste para encontrar uma assembleia no banco")
    public void testEncontrouAssembleiaIT() throws Exception {
        mockMvc.perform(get("/api/d59c555e-1a85-451b-8009-e155ce570b38"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAssembleia").value("d59c555e-1a85-451b-8009-e155ce570b38"))
                .andExpect(jsonPath("$.abertura").value("2023-01-30T14:00:10.123872"))
                .andExpect(jsonPath("$.fechamento").value("2023-01-30T15:00:10.123872"))
                .andExpect(jsonPath("$.status").value("FINALIZADO"));
    }
    @Test
    @DisplayName("Teste para encontrar uma assembleia invalida no banco")
    public void testAssembleiaNaoEncontradaIT() throws Exception {
        mockMvc.perform(get("/api/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Teste para criar uma assembleia no banco")
    public void testCriaAssembleiaIT() throws Exception {
        AssembleiaRequest assembleiaRequest = new AssembleiaRequest("Nova Assembleia");
        String novaAssembleia = mapper.writeValueAsString(assembleiaRequest);
        mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaAssembleia))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Teste para criar uma assembleia invalida no banco")
    public void testCriaAssembleiaInvalidaIT() throws Exception {
        String novaAssembleia = mapper.writeValueAsString(null);
        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaAssembleia))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Teste para adicionar uma pauta já presente em uma assembleia no banco")
    public void testAdicionarPautaRepetidaIT() throws Exception {
        ClienteRequest clienteRequest = new ClienteRequest(UUID.fromString("5ca07fd1-4419-401a-8ceb-58b3f92c24de"));
        String adicionaPauta = mapper.writeValueAsString(clienteRequest);
        mockMvc.perform(post("/api/adicionapauta/d59c555e-1a85-451b-8009-e155ce570b38")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adicionaPauta))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Teste para adicionar uma pauta inexistente em uma assembleia no banco")
    public void testAdicionarPautaInvalidaIT() throws Exception {
        ClienteRequest clienteRequest = new ClienteRequest(UUID.randomUUID());
        String adicionaPauta = mapper.writeValueAsString(clienteRequest);
        mockMvc.perform(post("/api/adicionapauta/d59c555e-1a85-451b-8009-e155ce570b38")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adicionaPauta))
                .andExpect(status().isNotFound());
    }

}
