package com.dbserver.desafiovotacao.service.implementacao;

import com.dbserver.desafiovotacao.dto.ClienteRequest;
import com.dbserver.desafiovotacao.dto.PautaRequest;
import com.dbserver.desafiovotacao.enums.PautaAndamentoEnum;
import com.dbserver.desafiovotacao.enums.PautaResultadoEnum;
import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.exception.AcaoInvalidaException;
import com.dbserver.desafiovotacao.exception.FalhaBuscaException;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;
import com.dbserver.desafiovotacao.repository.PautaRepositorio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import com.dbserver.desafiovotacao.service.PautaService;
import com.dbserver.desafiovotacao.service.VotanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PautaServiceImplementacao implements PautaService {

    private final VotanteService votanteService;
    private final PautaRepositorio pautaRepositorio;

    @Autowired
    public PautaServiceImplementacao(VotanteService votanteService, PautaRepositorio pautaRepositorio) {
        this.votanteService = votanteService;
        this.pautaRepositorio = pautaRepositorio;
    }

    @Override
    public Optional<Pauta> encontrarPautaPorID(UUID id){
        Optional<Pauta> resultado = pautaRepositorio.findById(id);
        if(resultado.isEmpty())
            throw new FalhaBuscaException("Pauta não encontrada");
        return resultado;
    }
    
    @Override
        public Optional<Pauta> encontrarPautaPorHash(String hash){
        Optional<Pauta> resultado = pautaRepositorio.findByHash(hash);
        if(resultado.isEmpty())
            throw new FalhaBuscaException("Pauta não encontrada");
        else
            return resultado;
    }

    @Override
    public Pauta salvarPauta(PautaRequest pautaRequest) {
        Optional<Votante> buscaAutor = votanteService.encontrarVotantePorID(pautaRequest.idAutor());
        return pautaRepositorio.save(Pauta.builder().titulo(pautaRequest.titulo())
                        .autorPauta(buscaAutor.get()).hash(geraHash()).andamento(PautaAndamentoEnum.APURANDO).build());
    }
    
    @Override
    public Page<Pauta> mostraPautas(Pageable pageable){
        return pautaRepositorio.findAll(pageable);
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
    public Pauta adicionarAssociado(String hashPauta, ClienteRequest clienteRequest) throws AcaoInvalidaException{
        Optional<Pauta> pauta = encontrarPautaPorHash(hashPauta);
        Optional<Votante> votante = votanteService.encontrarVotantePorID(clienteRequest.id());
        if(pauta.isEmpty()|| votante.isEmpty()) {
            throw new AcaoInvalidaException("Não é possivel continuar com a operação, pauta: "
                    + pauta.get().getId() + ", votante: " + votante.get().getId());
        }else{
            pauta.get().getAssociados().add(votante.get());
            return pautaRepositorio.save(pauta.get());
        }
    }

    @Override
    public Pauta finalizarPauta(String hash) {
        Optional<Pauta> pautaOptional = encontrarPautaPorHash(hash);
        if(pautaOptional.isPresent()){
            Pauta pauta = pautaOptional.get();
            Integer contSim = pauta.getAssociados().stream()
                    .filter(associado -> associado.getVoto() == VotoEnum.SIM)
                    .mapToInt(i -> 1).sum();
            Integer totalVotos = pauta.getAssociados().size();

            if (totalVotos == 0 || pauta.getFechamentoPauta().isAfter(LocalTime.now())) {
                pauta.setAndamento(PautaAndamentoEnum.APURANDO);
                throw new AcaoInvalidaException("Pauta ainda está sendo apurada");
            } else if (contSim >= (totalVotos / 2) + 1) {
                pauta.setResultado(PautaResultadoEnum.APROVADO);
            } else {
                pauta.setResultado(PautaResultadoEnum.INDEFERIDO);
            }

            pauta.setAndamento(PautaAndamentoEnum.CONCLUIDO);
            return pautaRepositorio.save(pauta);
        }
        throw new AcaoInvalidaException("Ação não pode ser concluida");

    }

    public String geraHash(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        byte[] hashBytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            hashBytes = digest.digest(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hash = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        hash = hash.substring(0, 8);
        return hash;
    }
}
