package com.security.pki.service;

import com.security.pki.dto.CertificateDto;
import com.security.pki.dto.CertificateNodeDto;
import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateStatus;
import com.security.pki.model.CertificateType;
import com.security.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;


    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<CertificateNodeDto> getAllCertificateNodes() {
        return this.certificateRepository.findAll().stream().map(certificate -> new CertificateNodeDto(certificate)).toList();
    }
    
    public CertificateDto getBySerialNumber(String serialNumber){
        System.out.println("coki");
        return new CertificateDto(this.certificateRepository.findCertificateBySerialNumber(serialNumber));
    }

    public CertificateDto getByAlias(String alias){
        Certificate certificate = this.certificateRepository.findCertificateByAlias(alias);
        if(certificate != null){
            return new CertificateDto(this.certificateRepository.findCertificateByAlias(alias));
        }

        return null;
    }


    public Boolean isValid(String serialNumber){
        Optional<Certificate> certificate = certificateRepository.findById(serialNumber);

        if(certificate.isEmpty()) {
            return false;
        }

        Date today = new Date();
        if(certificate.get().getType() == CertificateType.ROOT){
            return certificate.get().getStatus() == CertificateStatus.VALID && today.before(certificate.get().getValidTo());
        }
        return isValid(certificate.get().getIssuerSerialNumber()) &&
                certificate.get().getStatus() == CertificateStatus.VALID && today.before(certificate.get().getValidTo());
        }

}
