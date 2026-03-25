package com.jeffssousa.fluxo.exception.handler;

import com.jeffssousa.fluxo.exception.business.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<StandardError> emailAlreadyExistsException(EmailAlreadyExistsException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnauthorizedResourceAccessException.class)
    public ResponseEntity<StandardError> unauthorizedResourceAccess(UnauthorizedResourceAccessException e,HttpServletRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        log.warn("Acesso não permitido");

        return ResponseEntity.status(status).body(error);

    }

    @ExceptionHandler(TransactionNotFound.class)
    public ResponseEntity<StandardError> transactionNotFound(TransactionNotFound e,HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        log.warn("Transação não encontrada");

        return ResponseEntity.status(status).body(error);

    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<StandardError> invalidPassword(InvalidPasswordException e,HttpServletRequest request){
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        log.warn("usuario inseriu uma senha invalida");

        return ResponseEntity.status(status).body(error);

    }


    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<StandardError> passwordMismatchException(PasswordMismatchException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        log.info("Senhas não coincidem");

        return ResponseEntity.status(status).body(error);

    }

}
