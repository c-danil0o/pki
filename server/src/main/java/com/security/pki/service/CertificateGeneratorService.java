package com.security.pki.service;

import com.security.pki.exceptions.*;
import com.security.pki.model.*;
import com.security.pki.model.Certificate;
import com.security.pki.model.certificateData.Issuer;
import com.security.pki.model.certificateData.Subject;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import com.security.pki.util.CertificateBuilderUtils;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CertificateGeneratorService {
    private final CertificateRepository certificateRepository;
    private final CertificateBuilderUtils certificateBuilderUtils;
    private final TaskScheduler taskScheduler;
    private static final Logger LOG = Logger.getAnonymousLogger();

    @Autowired
    public CertificateGeneratorService(KeyStoreRepository keyStoreRepository, PrivateRepository privateRepository, CertificateRepository certificateRepository, CertificateBuilderUtils certificateBuilderUtils, TaskScheduler taskScheduler) {
        this.certificateRepository = certificateRepository;
        this.certificateBuilderUtils = certificateBuilderUtils;
        this.taskScheduler = taskScheduler;
        Security.addProvider(new BouncyCastleProvider());
    }


    public Certificate get(Request request) {
        if (request.getType()!=CertificateType.ROOT)
            validateRequest(request);
        Subject subject = certificateBuilderUtils.generateSubject(request);
        Issuer issuer = certificateBuilderUtils.generateIssuer(request.getSignerAlias(), subject.getPrivateKey(), request.getType());
        if (request.getValidFrom()==null){
            request.setValidFrom(new Date());
        }
        X509v3CertificateBuilder builder = certificateBuilderUtils.configureCertificateBuilder(subject.getX500Name(),
                issuer.getX500Name(), request.getValidFrom(), request.getValidTo(),
                String.valueOf(System.currentTimeMillis()), subject.getPublicKey());

        certificateBuilderUtils.addExtensions(builder, request.getExtensions(), subject.getPublicKey(), issuer.getCertificate() != null ? issuer.getCertificate().getPublicKey(): null);
        X509Certificate certificate = certificateBuilderUtils.signCertificate(builder, issuer.getPrivateKey());
        if (request.getType().equals(CertificateType.ROOT))
            issuer.setCertificate(certificate);
        certificateBuilderUtils.saveCertificateAndKeys(certificate, request.getAlias(), subject.getPrivateKey(), request.getType());
        Certificate certificateModel = new Certificate(certificate, issuer, request);
        certificateRepository.save(certificateModel);
        Runnable task1 = () -> makeCertificateValid(certificateModel);
        Runnable task2 = () -> makeCertificateInvalid(certificateModel);
        Instant startDate = certificateModel.getValidFrom().toInstant();
        Instant endDate = certificateModel.getValidTo().toInstant();
        taskScheduler.schedule(task1,startDate);
        taskScheduler.schedule(task2, endDate);
        return certificateModel;
    }

    private void validateRequest(Request request){
        if (certificateRepository.findCertificateByAlias(request.getAlias()) != null){
            throw new AliasAlreadyExistsException("Given alias already exists");
        }
        Certificate signer = certificateRepository.findCertificateByAlias(request.getSignerAlias());
        if (signer == null){
            throw new SignerNotFoundException("Given signer doesnt exist");
        }
        if (signer.getStatus() == CertificateStatus.INVALID || signer.getStatus() == CertificateStatus.REVOKED){
            throw new InvalidSignerException("Given signer is " + signer.getStatus().toString());
        }
        if (request.getValidFrom().before(signer.getValidFrom()) || request.getValidTo().after(signer.getValidTo()) || request.getValidFrom().after(request.getValidTo())){
            throw new InvalidDatesWithSigner("Given signer can't sign given dates");
        }
        for(String key : request.getExtensions().keySet()){
            if (!signer.getExtensions().containsKey(key)){
                throw new SignerHasLessExtensionsException("Signer has less extensions then subject");
            }
        }
        Map<String, String> keyUsageMap = request.getExtensions();
        Map<String,String> signerKeyUsageMap = signer.getExtensions();
        for (String c : keyUsageMap.get("keyUsage").split(",")) {
            if (!signerKeyUsageMap.get("keyUsages").contains(c)){
                throw new SignerHasLessExtensionsException("Signer has less extensions then subject");
            }
        }
    }

    @SneakyThrows
    private void makeCertificateInvalid(Certificate certificate){
        if(certificate.getStatus() == CertificateStatus.VALID){
            certificate.setStatus(CertificateStatus.INVALID);
            certificateRepository.save(certificate);
            LOG.log(Level.INFO, "Seting status to INVALID because Certificate" + certificate.getSerialNumber() + " has expired");
        }
    }

    @SneakyThrows
    private void makeCertificateValid(Certificate certificate){
        if(certificate.getStatus() == CertificateStatus.INVALID){
            certificate.setStatus(CertificateStatus.VALID);
            certificateRepository.save(certificate);
            LOG.log(Level.INFO, "Seting status to VALID - Certificate" + certificate.getSerialNumber());
        }
    }
  
    @PostConstruct
    public void onStartUp(){
        List<Certificate> certificates = certificateRepository.findAll();
        for (Certificate certificate: certificates) {
            if(DateUtils.isSameDay(certificate.getValidFrom(), new Date()) || certificate.getValidFrom().before(new Date()))
                makeCertificateValid(certificate);
            if(DateUtils.isSameDay(certificate.getValidTo(), new Date()) || certificate.getValidTo().before(new Date()))
                makeCertificateInvalid(certificate);
        }
    }
    


}
