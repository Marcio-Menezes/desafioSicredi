package com.dbserver.desafiovotacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OcorreuConflitoException extends RuntimeException{
    public OcorreuConflitoException(String mensagem) {
        super(mensagem);
    }
}
