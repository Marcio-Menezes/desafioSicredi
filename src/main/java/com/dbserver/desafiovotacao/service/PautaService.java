
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.model.Pauta;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;

public interface PautaService {
    Optional<Pauta> encontrarPautaPorID(UUID id) throws DataAccessException;
    Pauta encontrarPautaPorHash(String hash) throws DataAccessException;
    Pauta salvarPauta(Pauta pauta) throws DataAccessException;
    Integer totalVotantes(UUID id);
    Iterable<Pauta> mostraPautas();
    Optional<Pauta> updatePauta(Pauta pauta);
}
