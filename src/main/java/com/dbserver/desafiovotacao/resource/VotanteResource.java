
package com.dbserver.desafiovotacao.resource;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import java.util.UUID;

public class VotanteResource {
    private UUID id;
    private VotoEnum voto;

    public VotanteResource(UUID id, VotoEnum voto) {
        this.id = id;
        this.voto = voto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VotoEnum getVoto() {
        return voto;
    }

    public void setVoto(VotoEnum voto) {
        this.voto = voto;
    }
    
}
