package com.security.pki.service;

import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateStatus;
import com.security.pki.model.CertificateType;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateRevocationService {
    private final CertificateRepository certificateRepository;
    @Autowired
    public CertificateRevocationService(CertificateRepository certificateRepository, KeyStoreRepository keyStoreRepository, PrivateRepository privateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public void revokeCertificate(String serialNumber){
        Certificate certificate = certificateRepository.findById(serialNumber).orElseGet(null);
        if(certificate != null){
            certificate.setStatus(CertificateStatus.REVOKED);
            certificateRepository.save(certificate);
            revokeAllDescendents(serialNumber);
        }else
            throw new EntityNotFoundException("Element with given ID doesn't exist!");
    }

    private void revokeAllDescendents(String issuerSerialNumber){
        List<Certificate> certificateList = certificateRepository.findCertificateByIssuerSerialNumber(issuerSerialNumber);
        for (Certificate certificate : certificateList) {
            String serialNumber = certificate.getSerialNumber();
            certificate.setStatus(CertificateStatus.REVOKED);
            certificateRepository.save(certificate);
            if (!certificate.getType().equals(CertificateType.END))
                revokeAllDescendents(serialNumber);
        }
    }
}
