package com.jeffssousa.fluxo.exception.business;

public class UnauthorizedResourceAccessException extends RuntimeException {
    public UnauthorizedResourceAccessException(String message) {
        super(message);
    }
}
