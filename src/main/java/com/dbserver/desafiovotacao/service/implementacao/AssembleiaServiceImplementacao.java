
package com.dbserver.desafiovotacao.service.implementacao;

import com.dbserver.desafiovotacao.dto.AssembleiaRequest;
import com.dbserver.desafiovotacao.dto.AssembleiaResponse;
import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.enums.AssembleiaEnum;
import com.dbserver.desafiovotacao.enums.PautaAndamentoEnum;
import com.dbserver.desafiovotacao.exception.AcaoInvalidaException;
import com.dbserver.desafiovotacao.exception.FalhaBuscaException;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.repository.AssembleiaRepositorio;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import com.dbserver.desafiovotacao.service.AssembleiaService;
import com.dbserver.desafiovotacao.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssembleiaServiceImplementacao implements AssembleiaService {

    private final PautaService pautaService;
    private final AssembleiaRepositorio assembleiaRepositorio;
    
    @Autowired
    public AssembleiaServiceImplementacao(AssembleiaRepositorio assembleiaRepositorio, PautaService pautaService) {
        this.assembleiaRepositorio = assembleiaRepositorio;
        this.pautaService = pautaService;
    }
    @Override
    public Optional<Assembleia> encontrarAssembleiaPorID(UUID id){
        Optional<Assembleia> resultado = assembleiaRepositorio.findById(id);
        if(resultado.isEmpty()){
            throw new FalhaBuscaException("Assembleia não encontrada");
        }else{
            return resultado;
        }
    }

    @Override
    public Assembleia salvarAssembleia(AssembleiaRequest assembleiaRequest){
       return assembleiaRepositorio.save(Assembleia.builder().nomeAssembleia(assembleiaRequest.nomeAssembleia())
               .aberturaAssembleia(LocalDateTime.now()).status(AssembleiaEnum.MOVIMENTO)
               .listaPauta(new ArrayList<>()).build());
    }

    @Override
    public Integer totalPautas(UUID id) {
       Optional<Assembleia> assembleia = encontrarAssembleiaPorID(id);
        return assembleia.get().getListaPauta().stream().mapToInt(p -> 1).sum() == 0 ? 0 : assembleia.get().getListaPauta().size();
    }

    @Override
    public Page<Assembleia> mostraTudo(Pageable pageable) {
        return assembleiaRepositorio.findAll(pageable);
    }

    @Override
    public Iterable<Pauta> mostraPautas(UUID id) {
        Optional<Assembleia> assembleia = encontrarAssembleiaPorID(id);
        return assembleia.get().getListaPauta();
    }

    @Override
    public Assembleia adicionarPauta(UUID idAssembleia, ClienteRequest clienteRequest){
        Optional<Assembleia> assembleiaOptional = encontrarAssembleiaPorID(idAssembleia);
        Optional<Pauta> pauta = pautaService.encontrarPautaPorID(clienteRequest.id());
        Assembleia assembleia = assembleiaOptional.orElseThrow();
        pauta.ifPresent(p -> {
            if (assembleia.getListaPauta().contains(p)) {
                throw new AcaoInvalidaException("Pauta já existe na assembleia");
            }
            p.setAberturaPauta(LocalTime.now());
            p.setFechamentoPauta(LocalTime.now().plusMinutes(1));
            assembleia.getListaPauta().add(p);
        });

            return assembleiaRepositorio.save(assembleia);
    }

    @Override
    public Page<AssembleiaResponse> mostrarAssembleias(Pageable pageable){
        Page<Assembleia> assembleias = assembleiaRepositorio.findAll(pageable);
        List<AssembleiaResponse> assembleiasResponse = assembleias.stream()
                .map(AssembleiaResponse::new)
                .collect(Collectors.toList());
        return new PageImpl<>(assembleiasResponse, pageable, assembleias.getTotalElements());
    }

    @Override
    public Assembleia finalizarAssembleia(UUID id){
        Optional<Assembleia> assembleia = encontrarAssembleiaPorID(id);
        if(assembleia.isEmpty()){
            throw new FalhaBuscaException("Assembleia não encontrada");
        }
        assembleia.ifPresent(a -> {
            if (a.getStatus() == AssembleiaEnum.FINALIZADO) {
                throw new AcaoInvalidaException("A Assembleia já está finalizada");
            }

            boolean pautasConcluidas = a.getListaPauta().stream()
                    .allMatch(pauta -> pauta.getAndamento() == PautaAndamentoEnum.CONCLUIDO);

            if (!pautasConcluidas) {
                throw new AcaoInvalidaException("Ainda há pautas em andamento, finalize-as antes de fechar a assembleia");
            }
            a.setStatus(AssembleiaEnum.FINALIZADO);
            a.setEncerramentoAssembleia(LocalDateTime.now());
        });

        return assembleiaRepositorio.save(assembleia.get());


    }

}
