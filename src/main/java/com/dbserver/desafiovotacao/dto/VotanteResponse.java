package com.dbserver.desafiovotacao.dto;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.dbserver.desafiovotacao.model.Votante;

import java.util.UUID;

public record VotanteResponse(UUID id, String cpf, VotoEnum votoEnum) {
    public VotanteResponse(Votante votante){
        this(votante.getId(),votante.getCpf(),votante.getVoto());
    }
}
