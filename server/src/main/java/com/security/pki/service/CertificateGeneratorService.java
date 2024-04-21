package com.security.pki.service;

import com.security.pki.exceptions.InvalidDatesWithSigner;
import com.security.pki.exceptions.InvalidSignerException;
import com.security.pki.exceptions.SignerNotFoundException;
import com.security.pki.model.*;
import com.security.pki.model.Certificate;
import com.security.pki.model.certificateData.Issuer;
import com.security.pki.model.certificateData.Subject;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import com.security.pki.util.CertificateBuilderUtils;
import com.security.pki.util.ExtensionsUtils;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CertificateGeneratorService {
    private final KeyStoreRepository keyStoreRepository;
    private final PrivateRepository privateRepository;
    private final CertificateRepository certificateRepository;
    private final CertificateBuilderUtils certificateBuilderUtils;

    @Autowired
    public CertificateGeneratorService(KeyStoreRepository keyStoreRepository, PrivateRepository privateRepository, CertificateRepository certificateRepository, CertificateBuilderUtils certificateBuilderUtils) {
        this.certificateRepository = certificateRepository;
        this.certificateBuilderUtils = certificateBuilderUtils;
        Security.addProvider(new BouncyCastleProvider());
        this.keyStoreRepository = keyStoreRepository;
        this.privateRepository = privateRepository;
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
        return certificateModel;
    }

    private void validateRequest(Request request){
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
    }
  

    


}
