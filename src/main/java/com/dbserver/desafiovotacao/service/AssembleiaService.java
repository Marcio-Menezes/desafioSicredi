
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;


public interface AssembleiaService {
    Optional<Assembleia> encontrarAssembleiaPorID(UUID id) throws DataAccessException;
    Assembleia salvarAssembleia(Assembleia assembleia) throws DataAccessException;
    Integer totalPautas(UUID id);
    Iterable<Pauta> mostraPautas(String hash);
}
