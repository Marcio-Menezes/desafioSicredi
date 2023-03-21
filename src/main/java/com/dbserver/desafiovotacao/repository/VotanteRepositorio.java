
package com.dbserver.desafiovotacao.repository;

import com.dbserver.desafiovotacao.model.Votante;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VotanteRepositorio extends JpaRepository<Votante, UUID>{
    @Override
    public Votante save(Votante votante);
}
