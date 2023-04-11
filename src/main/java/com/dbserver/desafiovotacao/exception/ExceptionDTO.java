package com.dbserver.desafiovotacao.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDTO {
    private String erroTitulo;
    private String mensagem;
    private Integer cod;
    private LocalDateTime horaErro;
}
