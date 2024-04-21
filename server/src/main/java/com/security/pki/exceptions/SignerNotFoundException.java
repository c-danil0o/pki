package com.security.pki.exceptions;

public class SignerNotFoundException extends RuntimeException{
    public SignerNotFoundException(String message){
        super(message);
    }
}
