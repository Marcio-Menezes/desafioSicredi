package com.dbserver.desafiovotacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FalhaBuscaException extends RuntimeException{
    public FalhaBuscaException(String mensagem) {
        super(mensagem);
    }

}
