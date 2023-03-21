
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.enums.AssembleiaEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.AssembleiaRepositorio;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testar Assembleia Service")
public class AssembleiaServiceImplementacaoIT {
    
    @Mock
    private AssembleiaRepositorio assembleiaRepositorio;
    
    @InjectMocks
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
    public void testEncontrarAssembleiaPorIDSucesso() {
        when(assembleiaRepositorio.findById(assembleia.getId())).thenReturn(Optional.of(assembleia));
		Optional<Assembleia> resposta = assembleiaRepositorio.findById(assembleia.getId());
		assertEquals(Optional.of(assembleia), resposta);
    }
    
    @Test
    public void testEncontrarAssembleiaPorIDFalha() {
        UUID idRandom = UUID.randomUUID();
        when(assembleiaRepositorio.findById(idRandom)).thenReturn(Optional.of(assembleia));
		Optional<Assembleia> resposta = assembleiaRepositorio.findById(assembleia.getId());
		assertEquals(Optional.empty(), resposta);
    }

    @Test
    public void testSalvarAssembleia() {
        when(assembleiaRepositorio.save(assembleia)).thenReturn(assembleia);
		assembleiaService.salvarAssembleia(assembleia);
		verify(assembleiaRepositorio, times(1)).save(assembleia);
    }
    
    
}
