
package com.dbserver.desafiovotacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class AcaoInvalidaException extends RuntimeException{
    public AcaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
