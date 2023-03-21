/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.PautaRepositorio;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar Pauta Service")
public class PautaServiceImplementacaoIT {
    
    @Mock
    private PautaRepositorio pautaRepositorio;
    @InjectMocks
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
    @DisplayName("Testar encontrar pauta existente por ID")
    public void testEncontrarPautaPorIDSucesso() {
        when(pautaRepositorio.findById(pauta.getId())).thenReturn(Optional.of(pauta));
		Optional<Pauta> resposta = pautaRepositorio.findById(pauta.getId());
		assertEquals(Optional.of(pauta), resposta);
    }
    
    @Test
    @DisplayName("Testar encontrar pauta inexistente por ID")
    public void testEncontrarPautaPorIDFalha() {
        UUID idRandom = UUID.randomUUID();
        when(pautaRepositorio.findById(idRandom)).thenReturn(Optional.of(pauta));
		Optional<Pauta> resposta = pautaRepositorio.findById(pauta.getId());
		assertEquals(Optional.empty(), resposta);
    }

    @Test
    @DisplayName("Testar encontrar pauta existente por hash")
    public void testEncontrarPautaPorHashSucesso() {
        when(pautaRepositorio.findByHash(pauta.getHash())).thenReturn(Optional.of(pauta));
		Optional<Pauta> resposta = pautaRepositorio.findByHash(pauta.getHash());
		assertEquals(Optional.of(pauta), resposta);
    }
    
    @Test
    @DisplayName("Testar encontrar pauta inexistente por hash")
    public void testEncontrarPautaPorHashFalha() {
        
        String hashTeste = "TesteDeHash";
        when(pautaRepositorio.findByHash(hashTeste)).thenReturn(Optional.of(pauta));
		Optional<Pauta> resposta = pautaRepositorio.findByHash(pauta.getHash());
		assertEquals(Optional.empty(), resposta);
    }

    @Test
    @DisplayName("Testar o salvamento de uma pauta")
    public void testSalvarPauta() {
        when(pautaRepositorio.save(pauta)).thenReturn(pauta);
		pautaService.salvarPauta(pauta);
		verify(pautaRepositorio, times(1)).save(pauta);
    }



    @Test
    @DisplayName("Testar o update de uma pauta")
    public void testUpdatePauta() {
        when(pautaRepositorio.findById(pauta.getId())).thenReturn(Optional.of(pauta));
		pautaService.updatePauta(pauta);
		verify(pautaRepositorio, times(1)).save(pauta);
    }
    
}
