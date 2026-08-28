package com.jeffssousa.fluxo.exception.business;

public class TransactionNotFound extends RuntimeException {
    public TransactionNotFound(String message) {
        super(message);
    }
}
