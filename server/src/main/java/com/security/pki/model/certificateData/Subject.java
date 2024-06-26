package com.security.pki.model.certificateData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Setter
@AllArgsConstructor
public class Subject {

    private X500Name x500Name;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    public Subject(X500Name name){
        x500Name = name;
    }
}
