package com.security.pki.model;


import com.security.pki.model.certificateData.Issuer;
import com.security.pki.model.certificateData.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jcajce.provider.asymmetric.dh.BCDHPrivateKey;

import java.beans.ConstructorProperties;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Certificate {
    @Id
    private String serialNumber;
    @Column
    private String issuerSerialNumber;
    @Column
    private String issuerName;
    @Column
    private String subjectName;
    @Column
    private String subjectEmail;
    @Column
    @Temporal(TemporalType.DATE)
    private Date validFrom;
    @Column
    @Temporal(TemporalType.DATE)
    private Date validTo;
    @Column
    @Enumerated(EnumType.STRING)
    private CertificateType type;
    @Column
    @Enumerated(EnumType.STRING)
    private CertificateStatus status;
    @Column
    private String signatureAlgorithm;
    @Column
    private String alias;
    @ElementCollection
    private Map<String, String> extensions;

    
    public Certificate(X509Certificate x509Certificate, Issuer issuer, Request request){
        this.serialNumber = x509Certificate.getSerialNumber().toString();
        this.issuerSerialNumber = issuer.getCertificate().getSerialNumber().toString();
        this.issuerName = issuer.getX500Name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString(); // provjeriti
        this.subjectEmail = request.getEmail();
        this.validTo = x509Certificate.getNotAfter();
        this.validFrom = x509Certificate.getNotBefore();
        this.type = request.getType();
        this.status = CertificateStatus.INVALID;
        this.signatureAlgorithm = x509Certificate.getSigAlgName();
        this.alias = request.getAlias();
        this.extensions = request.getExtensions();
        this.subjectName = request.getFirstName() + " " + request.getLastName();
        
    }
    
}
