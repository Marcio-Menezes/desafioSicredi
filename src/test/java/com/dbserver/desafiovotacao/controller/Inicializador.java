package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Assembleia;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Votante;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Inicializador {

    public Assembleia construirAssembleia(){
        Pauta pauta = construirPauta();
        Assembleia assembleia =  Assembleia.builder().id(UUID.randomUUID()).aberturaAssembleia(LocalDateTime.now()).listaPauta(new ArrayList<>()).nomeAssembleia("Teste Assembleia").build();
        assembleia.getListaPauta().add(pauta);
        return assembleia;
    }

    public Pauta construirPauta(){
        Votante votante = construirVotante();
        Votante votanteAutor = construirVotanteAutor();
        Pauta pauta = Pauta.builder().id(UUID.randomUUID()).titulo("Teste").descricao("Esse é um teste unitário").associados(new ArrayList<>()).autorPauta(votanteAutor).hash("2h-23bh5").build();
        pauta.getAssociados().add(votante);
        return pauta;
    }

    public Votante construirVotante(){
        return Votante.builder().id(UUID.randomUUID()).cpf("75871151027").voto(VotoEnum.SIM).build();
    }

    public Votante construirVotanteAutor(){
        return Votante.builder().id(UUID.randomUUID()).cpf("28384806004").voto(VotoEnum.AUTORIA).build();
    }
}
