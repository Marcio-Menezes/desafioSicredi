
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.dto.PautaRequest;
import com.dbserver.desafiovotacao.model.Pauta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PautaService {
    Optional<Pauta> encontrarPautaPorID(UUID id);
    Optional<Pauta> encontrarPautaPorHash(String hash);
    Pauta salvarPauta(PautaRequest pautaRequest);
    Integer totalVotantes(UUID id);
    Page<Pauta> mostraPautas(Pageable pageable);
    Pauta adicionarAssociado(String hashPauta, ClienteRequest clienteRequest);
    Pauta finalizarPauta(String hash);
}
