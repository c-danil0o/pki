package com.security.pki.dto;

import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateStatus;
import com.security.pki.model.CertificateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class CertificateDto {
    private String serialNumber;
    private String issuerSerialNumber;
    private String issuerName;
    private String subjectName;
    private String subjectEmail;
    private Date validFrom;
    private Date validTo;
    private String type;
    private String  status;
    private String signatureAlgorithm;
    private String alias;
    private Map<String, String> extensions;
    
    public CertificateDto(Certificate certificate){
        this.serialNumber = certificate.getSerialNumber();
        this.issuerSerialNumber = certificate.getIssuerSerialNumber();
        this.issuerName = certificate.getIssuerName();
        this.subjectName = certificate.getSubjectName();
        this.subjectEmail = certificate.getSubjectEmail();
        this.validFrom = certificate.getValidFrom();
        this.validTo = certificate.getValidTo();
        this.type = certificate.getType().toString();
        this.status = certificate.getStatus().toString();
        this.signatureAlgorithm = certificate.getSignatureAlgorithm();
        this.alias = certificate.getAlias();
        this.extensions = certificate.getExtensions();
    }
}
