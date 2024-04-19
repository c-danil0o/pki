package com.security.pki.repository;

import com.security.pki.model.certificateData.Issuer;
import org.bouncycastle.asn1.mozilla.PublicKeyAndChallenge;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Repository;

import javax.security.auth.x500.X500PrivateCredential;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Repository
public class KeyStoreRepository {
    private KeyStore keyStore;
    private String keyStoreFile = "src/main/resources/keystore/keystore.jks";

    public KeyStoreRepository() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

//    public Issuer readIssuer(String alias, char[] password, char[] keyPass) {
//        try {
//            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
//            keyStore.load(in, password);
//            Certificate cert = keyStore.getCertificate(alias);
//            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass);
//            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
//            return new Issuer(issuerName, new X500PrivateCredential((X509Certificate) cert, privateKey, alias));
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public Certificate readCertificate(String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
            if (ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void loadKeyStore(String fileName, char[] password) {
        try {
            if (fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCertificate(String alias, X509Certificate x509Certificate){
        try {
            keyStore.setCertificateEntry(alias, x509Certificate);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void deleteCertificate(String alias){
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

}
