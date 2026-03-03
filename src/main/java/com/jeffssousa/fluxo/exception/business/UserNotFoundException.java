package com.jeffssousa.fluxo.exception.business;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg){
        super(msg);
    }

}
