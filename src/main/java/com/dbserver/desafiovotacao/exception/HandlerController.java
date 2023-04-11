package com.dbserver.desafiovotacao.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
@ControllerAdvice
public class HandlerController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return new ResponseEntity<>(
                ExceptionDTO.builder()
                        .erroTitulo("Ação Invalida")
                        .mensagem("Reveja se os campos foram preenchidos corretamente")
                        .cod(HttpStatus.BAD_REQUEST.value())
                        .horaErro(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(FalhaBuscaException.class)
    public ResponseEntity<ExceptionDTO> handlerNaoEncontrado(FalhaBuscaException mensagem) {
        return new ResponseEntity<>(
                ExceptionDTO.builder()
                        .erroTitulo("Dado não encontrado")
                        .mensagem(mensagem.getMessage())
                        .cod(HttpStatus.NOT_FOUND.value())
                        .horaErro(LocalDateTime.now())
                        .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AcaoInvalidaException.class)
    public ResponseEntity<ExceptionDTO> handlerAcaoInvalida(AcaoInvalidaException mensagem) {
        return new ResponseEntity<>(
                ExceptionDTO.builder()
                        .erroTitulo("Ação não permitida")
                        .mensagem(mensagem.getMessage())
                        .cod(HttpStatus.METHOD_NOT_ALLOWED.value())
                        .horaErro(LocalDateTime.now())
                        .build(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(OcorreuConflitoException.class)
    public ResponseEntity<ExceptionDTO> handlerOcorreuConflito(OcorreuConflitoException mensagem) {
        return new ResponseEntity<>(
                ExceptionDTO.builder()
                        .erroTitulo("Requisição invalida")
                        .mensagem(mensagem.getMessage())
                        .cod(HttpStatus.BAD_REQUEST.value())
                        .horaErro(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
