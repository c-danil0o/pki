package com.security.pki.model.certificateData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import javax.security.auth.x500.X500PrivateCredential;

@Getter
@Setter
@AllArgsConstructor
public class Issuer {
    private X500Name x500Name;
    private X500PrivateCredential credential;
    
}
