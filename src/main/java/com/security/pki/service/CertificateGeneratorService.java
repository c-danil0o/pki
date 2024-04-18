package com.security.pki.service;

import com.security.pki.model.Certificate2;
import com.security.pki.model.certificateData.Issuer;
import com.security.pki.model.certificateData.Subject;
import com.security.pki.model.User;
import com.security.pki.repository.KeyStoreRepository;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class CertificateGeneratorService {
    private final KeyStoreRepository keyStoreRepository;

    public CertificateGeneratorService(KeyStoreRepository keyStoreRepository) {
        Security.addProvider(new BouncyCastleProvider());
        this.keyStoreRepository = keyStoreRepository;
    }
//
//    public Certificate2 getNewCertificate(User user, PublicKey publicKey, String issuerAlias) {
//        Subject subject = generateSubject(user, publicKey);
//        Issuer issuer = keyStoreRepository.readIssuer(issuerAlias, "password".toCharArray(), "keypass".toCharArray());
//        Date validFrom = Date.from(Instant.now());
//        Date validTo = Date.from(Instant.now().plus(20, ChronoUnit.CENTURIES));
//
//
//        X509Certificate newCertificate = generateCertificate(subject, issuer, validFrom, validTo, String.valueOf(System.currentTimeMillis()));
//        keyStoreRepository.write(user.getAlias(), issuer.getCredential().getPrivateKey(), "password".toCharArray(), newCertificate);
//        return new Certificate2(user.getAlias(), newCertificate, subject, issuer, validFrom, validTo, String.valueOf(System.currentTimeMillis()));
//    }
//
//    public Certificate2 generateRootCA() {
//
//        // generate keypair and subject same as issuer
//        // save root cert
//
//
//    }

    public Subject generateSubject(User user, PublicKey publicKey) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, user.getFirstName() + "_" + user.getLastName());
        builder.addRDN(BCStyle.SURNAME, user.getLastName());
        builder.addRDN(BCStyle.GIVENNAME, user.getFirstName());
        builder.addRDN(BCStyle.O, user.getOrganisation());
        builder.addRDN(BCStyle.C, user.getCountryCode());
        builder.addRDN(BCStyle.E, user.getEmail());
        builder.addRDN(BCStyle.UID, user.getAccountId().toString());
        return new Subject(builder.build(), publicKey);
    }


    public static X509Certificate generateCertificate(Subject subject, Issuer issuer, Date startDate, Date endDate, String serialNumber) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuer.getCredential().getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer.getX500Name(),
                    new BigInteger(serialNumber),
                    startDate,
                    endDate,
                    subject.getX500Name(),
                    subject.getPublicKey());

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
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

  
}
