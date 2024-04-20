package com.security.pki.service;

import com.security.pki.dto.CertificateDto;
import com.security.pki.dto.CertificateNodeDto;
import com.security.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
