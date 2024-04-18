package com.security.pki.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Date;

@Data   
@AllArgsConstructor
public class Certificate2 {
    private String alias;
    private Date validFrom;
    private Date validTo;
    private BigInteger serialNumber;
    private X500Name subject;
    private X500Name issuer;
    
    public Certificate2(X509Certificate x509Certificate){
        this.subject = X500Name.getInstance(x509Certificate.getSubjectX500Principal());
        this.issuer = X500Name.getInstance(x509Certificate.getIssuerX500Principal());
        this.serialNumber = x509Certificate.getSerialNumber();
        this.validFrom = x509Certificate.getNotBefore();
        this.validTo = x509Certificate.getNotAfter();
    }
}
