package com.security.pki.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class Certificate {
    private String issuerSerialNumber;
    private String serialNumber;
    private String issuerName;
    private String subjectEmail;
    private LocalDate validFrom;
    private LocalDate validTo;
    private CertificateType type;
    private CertificateStatus status;
    private String signatureAlgorithm;
    private String alias;
    private List<String> keyUsages;
    private List<String> extendedKeyUsages;
    
}
