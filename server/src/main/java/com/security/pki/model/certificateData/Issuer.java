package com.security.pki.model.certificateData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import javax.security.auth.x500.X500PrivateCredential;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Getter
@Setter
@AllArgsConstructor
public class Issuer {
    private X500Name x500Name;
    private String alias;
    private PrivateKey privateKey;
    private X509Certificate certificate;
    
    public Issuer(X500Name x500Name, String alias, PrivateKey privateKey){
        this.x500Name = x500Name;
        this.alias = alias;
        this.privateKey = privateKey;
    }
}
