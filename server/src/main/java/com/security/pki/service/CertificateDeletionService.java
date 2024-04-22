package com.security.pki.service;

import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateType;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateDeletionService {
    private final CertificateRepository certificateRepository;
    private final KeyStoreRepository keyStoreRepository;
    private final PrivateRepository privateRepository;
    private String keyStorePassword;

    @Autowired
    public CertificateDeletionService(CertificateRepository certificateRepository, KeyStoreRepository keyStoreRepository, PrivateRepository privateRepository) {
        this.certificateRepository = certificateRepository;
        this.keyStoreRepository = keyStoreRepository;
        this.privateRepository = privateRepository;
    }

    public void deleteCertificate(String serialNumber){
        Certificate certificate = certificateRepository.findById(serialNumber).orElseGet(null);
        if(certificate != null){
            certificateRepository.deleteById(serialNumber);
            keyStorePassword = privateRepository.getPassword("keystore");
            keyStoreRepository.loadKeyStore("keystore", keyStorePassword.toCharArray());
            keyStoreRepository.deleteCertificate(certificate.getAlias());
            privateRepository.deleteKey(certificate.getAlias());
            deleteAllDescendents(serialNumber);
        }else
            throw new EntityNotFoundException("Element with given ID doesn't exist!");
    }

    private void deleteAllDescendents(String issuerSerialNumber){
        List<Certificate> certificateList = certificateRepository.findCertificateByIssuerSerialNumber(issuerSerialNumber);
        for (Certificate certificate : certificateList) {
            String serialNumber = certificate.getSerialNumber();
            certificateRepository.deleteById(serialNumber);
            keyStoreRepository.deleteCertificate(certificate.getAlias());
            privateRepository.deleteKey(certificate.getAlias());
            if (!certificate.getType().equals(CertificateType.END))
                deleteAllDescendents(serialNumber);
        }
        keyStoreRepository.saveKeyStore("keystore", keyStorePassword.toCharArray());
    }
}
