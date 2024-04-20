package com.security.pki.util;

import com.security.pki.model.CertificateType;
import com.security.pki.model.Request;
import com.security.pki.model.certificateData.Issuer;
import com.security.pki.model.certificateData.Subject;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.security.auth.x500.X500PrivateCredential;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CertificateBuilderUtils {
    private final KeyStoreRepository keyStoreRepository;
    private final PrivateRepository privateRepository;

    public CertificateBuilderUtils(KeyStoreRepository keyStoreRepository, PrivateRepository privateRepository) {
        this.keyStoreRepository = keyStoreRepository;
        this.privateRepository = privateRepository;
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Subject generateSubject(Request request) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, request.getFirstName() + "_" + request.getLastName());
        builder.addRDN(BCStyle.SURNAME, request.getLastName());
        builder.addRDN(BCStyle.GIVENNAME, request.getFirstName());
        builder.addRDN(BCStyle.O, request.getOrganisation());
        builder.addRDN(BCStyle.C, request.getCountryCode());
        builder.addRDN(BCStyle.E, request.getEmail());
        if (request.getAccountId() == null){
            builder.addRDN(BCStyle.UID, UUID.randomUUID().toString());
        }else{
            builder.addRDN(BCStyle.UID, request.getAccountId().toString());
        }
        Subject subject = new Subject(builder.build());
        if (request.getType() == CertificateType.ROOT || request.getPublicKey() == null) {
            KeyPair keyPair = generateKeyPair();
            subject.setPublicKey(keyPair.getPublic());
            subject.setPrivateKey(keyPair.getPrivate());
        }else {
            subject.setPublicKey(getPublicKeyFromPem(request.getPublicKey()));
        }

        return subject;
    }

    public Issuer generateIssuer(String alias, PrivateKey key, CertificateType certificateType) {
        PrivateKey issuerPrivateKey = null;
        X500Name issuerName = null;
        if (certificateType.equals(CertificateType.ROOT) && key != null) {                          //ne valja (za intermediate ce da uzme njegov kljuc da je issuerov )
            issuerName = getRootIssuer();
            issuerPrivateKey = key;
            return new Issuer(issuerName, alias, issuerPrivateKey);
        } else {

            java.security.cert.Certificate issuerCertificate = keyStoreRepository.readCertificate("keystore", alias,
                    privateRepository.getPassword("keystore"));
            issuerPrivateKey = privateRepository.getKey(alias);


            X509Certificate certificate = (X509Certificate) issuerCertificate;
            try {
                issuerName = new JcaX509CertificateHolder(certificate).getSubject();
            } catch (CertificateEncodingException e) {
                throw new RuntimeException(e);
            }
            return new Issuer(issuerName, alias, issuerPrivateKey, certificate);
        }
    }

    public X500Name getRootIssuer() {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "Certifikey Co");
        builder.addRDN(BCStyle.SURNAME, "Co");
        builder.addRDN(BCStyle.GIVENNAME, "Certifikey");
        builder.addRDN(BCStyle.O, "tim12");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "certifikey@gmail.com");
        builder.addRDN(BCStyle.UID, "0");
        return builder.build();
    }

    public PublicKey getPublicKeyFromPem(String pem) {
        String publicKeyPEM = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.decodeBase64(publicKeyPEM);

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public X509Certificate signCertificate(X509v3CertificateBuilder builder, PrivateKey privateKey) {
        try {
            JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            signerBuilder = signerBuilder.setProvider("BC");
            ContentSigner contentSigner = signerBuilder.build(privateKey);
            X509CertificateHolder certHolder = builder.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            return certConverter.getCertificate(certHolder);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public X509v3CertificateBuilder configureCertificateBuilder(X500Name subject, X500Name issuer, Date startDate, Date endDate,
                                                                String serialNumber, PublicKey subjectPublicKey) {
        return new JcaX509v3CertificateBuilder(issuer,
                new BigInteger(serialNumber),
                startDate,
                endDate,
                subject,
                subjectPublicKey);
    }

    public void saveCertificateAndKeys(X509Certificate certificate, String alias, PrivateKey key, CertificateType type) {
        String password = null;
        if (type == CertificateType.ROOT) {
            password = UUID.randomUUID().toString();
            keyStoreRepository.loadKeyStore(null, password.toCharArray());
            privateRepository.savePassword("keystore", password);
        } else {
            password = privateRepository.getPassword("keystore");
            keyStoreRepository.loadKeyStore("keystore", password.toCharArray());
        }

        keyStoreRepository.saveCertificate(alias, certificate);
        keyStoreRepository.saveKeyStore("keystore", password.toCharArray());

        if (key != null)
            privateRepository.saveKey(key, alias);
    }

    public X509Certificate addExtensions(X509v3CertificateBuilder builder, Map<String, String> extensions, PublicKey subjectPublicKey, PublicKey issuerPublicKey) {
        ExtensionsUtils extensionsUtils = new ExtensionsUtils();
        for (Map.Entry<String, String> mapEntry: extensions.entrySet()) {
            extensionsUtils.addExtension(builder, mapEntry.getKey(), mapEntry.getValue(), subjectPublicKey, issuerPublicKey);
        }
        return null;
    }

}
