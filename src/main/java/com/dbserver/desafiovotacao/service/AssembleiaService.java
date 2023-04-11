
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.AssembleiaResponse;
import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AssembleiaService {
    Optional<Assembleia> encontrarAssembleiaPorID(UUID id);
    Assembleia salvarAssembleia(AssembleiaRequest assembleiaRequest);
    Integer totalPautas(UUID id);
    Page<Assembleia> mostraTudo(Pageable pageable);
    Page<AssembleiaResponse> mostrarAssembleias(Pageable pageable);
    Iterable<Pauta> mostraPautas(UUID id);
    Assembleia adicionarPauta(UUID idAssembleia, ClienteRequest clienteRequest);
    Assembleia finalizarAssembleia(UUID id);
}
