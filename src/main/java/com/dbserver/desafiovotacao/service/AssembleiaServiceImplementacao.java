
package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.repository.AssembleiaRepositorio;
import com.dbserver.desafiovotacao.repository.PautaRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AssembleiaServiceImplementacao implements AssembleiaService{

    private final PautaRepositorio pautaRepositorio;
    private final AssembleiaRepositorio assembleiaRepositorio;
    
    @Autowired
    public AssembleiaServiceImplementacao(AssembleiaRepositorio assembleiaRepositorio, PautaRepositorio pautaRepositorio) {
        this.assembleiaRepositorio = assembleiaRepositorio;
        this.pautaRepositorio = pautaRepositorio;
    }
    @Override
    public Optional<Assembleia> encontrarAssembleiaPorID(UUID id) throws DataAccessException {
        return assembleiaRepositorio.findById(id);
    }

    @Override
    public Assembleia salvarAssembleia(Assembleia assembleia) throws DataAccessException {
       return assembleiaRepositorio.save(assembleia);
    }

    @Override
    public Integer totalPautas(UUID id) {
       Optional<Assembleia> assembleiaOpcional = assembleiaRepositorio.findById(id);
        return assembleiaOpcional.get().getListaPauta().size();
    }

    @Override
    public Iterable<Pauta> mostraPautas(String hash) {
        return pautaRepositorio.findAll();
    }
    
}
