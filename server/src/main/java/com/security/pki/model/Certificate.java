package com.security.pki.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;

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
    private String subjectEmail;
    @Column
    private LocalDate validFrom;
    @Column
    private LocalDate validTo;
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
    private List<String> keyUsages;
    @ElementCollection
    private List<String> extendedKeyUsages;
    
}
