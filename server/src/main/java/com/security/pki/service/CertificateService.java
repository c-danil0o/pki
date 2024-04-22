package com.security.pki.service;

import com.security.pki.dto.CertificateDto;
import com.security.pki.dto.CertificateNodeDto;
import com.security.pki.exceptions.CertificateNotApprovedException;
import com.security.pki.exceptions.RequestNotFoundException;
import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateStatus;
import com.security.pki.model.CertificateType;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import com.security.pki.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final KeyStoreRepository keyStoreRepository;
    private final PrivateRepository privateRepository;
    private final RequestRepository requestRepository;

    public CertificateService(CertificateRepository certificateRepository, KeyStoreRepository keyStoreRepository,
                              PrivateRepository privateRepository, RequestRepository requestRepository) {
        this.certificateRepository = certificateRepository;
        this.keyStoreRepository = keyStoreRepository;
        this.privateRepository = privateRepository;
        this.requestRepository = requestRepository;
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

    public String getCertificatePem(String alias){
        if(certificateRepository.findCertificateByAlias(alias)==null){
            if (requestRepository.findRequestByAlias(alias)!=null)
                throw new CertificateNotApprovedException("Your certificate is not approved yet");
            else {
                throw new RequestNotFoundException("There is no request with your alias");
            }
        }

        java.security.cert.Certificate certificate = keyStoreRepository.readCertificate("keystore", alias,
                privateRepository.getPassword("keystore"));

        String pemCertificate;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            outStream.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            outStream.write(Base64.getEncoder().encode(certificate.getEncoded()));
            outStream.write("\n-----END CERTIFICATE-----\n".getBytes());
            pemCertificate = outStream.toString();
            return pemCertificate;
        } catch (CertificateEncodingException | IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
