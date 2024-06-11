package com.security.pki.exceptions;

public class CertificateNotApprovedException extends RuntimeException{
    public CertificateNotApprovedException(String message){super(message);}
}
