
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.VotanteRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
@Service
public class VotanteServiceImplementacao implements VotanteService{
    private final VotanteRepositorio votanteRepositorio;
    
    @Autowired
	public VotanteServiceImplementacao(VotanteRepositorio votanteRepositorio) {
		this.votanteRepositorio = votanteRepositorio;

	}
        
    @Override
	public Optional<Votante> findById(UUID id) throws DataAccessException {
		return votanteRepositorio.findById(id);
	}
        
    @Override
	public int totalVotantes() {
		List<Votante> lista = (List<Votante>) this.votanteRepositorio.findAll();
		if(lista.isEmpty()) {
			return 0;
		}
		return lista.size();
	}
    @Override
	public Iterable<Votante> findAll() {
		return this.votanteRepositorio.findAll();
	}
        
    @Override
    public Votante salvarVotante(Votante votante) throws DataAccessException {
        return votanteRepositorio.save(votante);
    }
}
