package com.security.pki.dto;

import com.security.pki.model.Certificate;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateNodeDto {
    private String id;
    private String parentId;
    private String alias;
    private String issuerName;
    private String subjectEmail;
    private String type;
    private String status;
    private Date validTo;
    
    
    public CertificateNodeDto(Certificate certificate)
    {
        this.id = certificate.getSerialNumber();
        this.parentId = certificate.getIssuerSerialNumber();
        this.alias = certificate.getAlias();
        this.issuerName = certificate.getIssuerName();
        this.subjectEmail = certificate.getSubjectEmail();
        this.type = certificate.getType().toString();
        this.status = certificate.getStatus().toString();
        this.validTo = certificate.getValidTo();
    }
}
