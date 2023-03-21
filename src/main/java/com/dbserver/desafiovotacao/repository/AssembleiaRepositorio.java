
package com.dbserver.desafiovotacao.repository;

import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.resource.AssembleiaResource;

import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AssembleiaRepositorio extends CrudRepository<Assembleia, UUID> {
    @Override
    Optional<Assembleia> findById(UUID id) throws DataAccessException;
    public AssembleiaResource save(AssembleiaResource pautaResource);
}
