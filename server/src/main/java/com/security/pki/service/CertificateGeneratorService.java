package com.security.pki.service;

import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateStatus;
import com.security.pki.model.CertificateType;
import com.security.pki.model.certificateData.Issuer;
import com.security.pki.model.certificateData.Subject;
import com.security.pki.model.User;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
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
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Service
public class CertificateGeneratorService {
    private final KeyStoreRepository keyStoreRepository;
    private final PrivateRepository privateRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateGeneratorService(KeyStoreRepository keyStoreRepository, PrivateRepository privateRepository, CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
        Security.addProvider(new BouncyCastleProvider());
        this.keyStoreRepository = keyStoreRepository;
        this.privateRepository = privateRepository;
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

    public Certificate generateRootCertificate() {
        try {
            String serialNumber = Long.toString(System.currentTimeMillis());
            Certificate cert = new Certificate(serialNumber, serialNumber, "root", "root@gmail.com", LocalDate.now(), LocalDate.now().plusYears(1),
                    CertificateType.ROOT, CertificateStatus.VALID, "SHA256WithRSAEncryption", "root", new ArrayList<>(), new ArrayList<>());

            KeyPair rootKeyPair = generateKeyPair();
            String rootSubjectName = "CN=RootCA, OU=IT, O=PkiOrganization, L=Novi Sad, ST=Serbia, C=Serbia";
            Date startDate = new Date(System.currentTimeMillis());
            Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000);

            // Create the X500Name object for the subject
            X500Name subjectName = new X500Name(rootSubjectName);
            X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                    subjectName,
                    new BigInteger(serialNumber), // Serial number
                    startDate,
                    endDate,
                    subjectName, // The issuer is the same as the subject for a self-signed certificate
                    rootKeyPair.getPublic()
            );
            addRootExtensions(certBuilder, rootKeyPair);

            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
                    .build(rootKeyPair.getPrivate());
            X509CertificateHolder certificateHolder = certBuilder.build(contentSigner);

            X509Certificate x509Certificate = new JcaX509CertificateConverter().getCertificate(certificateHolder);
            keyStoreRepository.loadKeyStore(null, "password".toCharArray());
            keyStoreRepository.saveCertificate("root",x509Certificate);
            privateRepository.saveKey(rootKeyPair.getPrivate(), "rootPK");
            keyStoreRepository.saveKeyStore("src/main/resources/keystore/keystore.jks", "password".toCharArray());
            certificateRepository.save(cert);
            return cert;

        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private void addRootExtensions(X509v3CertificateBuilder certBuilder, KeyPair keyPair){
        try{
            certBuilder.addExtension(
                    Extension.keyUsage,
                    true,
                    new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
            );

            certBuilder.addExtension(
                    Extension.basicConstraints,
                    true,
                    new BasicConstraints(true)
            );

            ASN1InputStream asn1InputStream = new ASN1InputStream(keyPair.getPublic().getEncoded());
            ASN1Sequence publicKeyInfoSequence = DERSequence.getInstance(asn1InputStream.readObject());
            asn1InputStream.close();
            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKeyInfoSequence);
            byte[] subjectKeyIdentifier = new DEROctetString(subjectPublicKeyInfo.getPublicKeyData().getBytes()).getEncoded();

            certBuilder.addExtension(
                    Extension.subjectKeyIdentifier,
                    false,
                    new SubjectKeyIdentifier(subjectKeyIdentifier)
            );
        } catch (CertIOException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


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
