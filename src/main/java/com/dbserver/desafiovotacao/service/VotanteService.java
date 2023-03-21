
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.model.Votante;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;

public interface VotanteService {
    Optional<Votante> findById(UUID id) throws DataAccessException;
	int totalVotantes();
	Iterable<Votante> findAll();
        Votante salvarVotante(Votante votante) throws DataAccessException;
}
