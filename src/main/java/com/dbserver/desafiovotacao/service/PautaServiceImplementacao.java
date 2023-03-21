package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.enums.PautaResultadoEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.PautaRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PautaServiceImplementacao implements PautaService {

    private final PautaRepositorio pautaRepositorio;

    @Autowired
    public PautaServiceImplementacao(PautaRepositorio pautaRepositorio) {
        this.pautaRepositorio = pautaRepositorio;
    }

    @Override
    public Optional<Pauta> encontrarPautaPorID(UUID id) throws DataAccessException {
        return pautaRepositorio.findById(id);
    }
    
    @Override
    public Pauta encontrarPautaPorHash(String hash) throws DataAccessException {
        return pautaRepositorio.findByHash(hash).orElseThrow();
    }

    @Override
    public Pauta salvarPauta(Pauta pauta) throws DataAccessException {
        return pautaRepositorio.save(pauta);
    }
    
    @Override
    public Iterable<Pauta> mostraPautas(){
        return pautaRepositorio.findAll();
    }

    @Override
    public Integer totalVotantes(UUID id) {
        Optional<Pauta> pautaOpcional = pautaRepositorio.findById(id);
        if (pautaOpcional.isEmpty()) {
            return 0;
        }
        return pautaOpcional.get().getAssociados().size();
    }

    @Override
    public Optional<Pauta> updatePauta(Pauta pauta) {
        Optional<Pauta> atualizaPauta = pautaRepositorio.findById(pauta.getId());
        if (atualizaPauta.isPresent()) {
            PautaResultadoEnum resultadoAtualizado;
            if(contabilizaVotos(pauta)>= (pauta.getAssociados().size()/2)){
                resultadoAtualizado = PautaResultadoEnum.APROVADO;
            }else{
                resultadoAtualizado = PautaResultadoEnum.INDEFERIDO;
            }
            atualizaPauta.get().setResultado(resultadoAtualizado);
        }
        return atualizaPauta;
    }

    public Integer contabilizaVotos(Pauta pauta) {
        Integer contagemVotos = 0;
        contagemVotos = pauta.getAssociados().stream().filter(associado -> (associado.getVoto().equals(VotoEnum.SIM))).map(i -> 1).reduce(contagemVotos, Integer::sum);
        return contagemVotos;
    }
}
