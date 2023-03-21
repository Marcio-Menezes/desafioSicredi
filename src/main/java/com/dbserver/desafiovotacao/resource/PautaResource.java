
package com.dbserver.desafiovotacao.resource;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PautaResource {
    @JsonProperty("titulo")
    private String titulo;
    private VotoEnum resultado;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public VotoEnum getResultado() {
        return resultado;
    }

    public void setResultado(VotoEnum resultado) {
        this.resultado = resultado;
    }
    
    
}
