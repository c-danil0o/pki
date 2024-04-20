package com.security.pki.util;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.ContentSigner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class ExtensionsUtils {

    public void addExtension(X509v3CertificateBuilder builder,  String key, String value, PublicKey subjectPublicKey, PublicKey issuerPublicKey){
        try {
            switch (key) {
                case "basic":
                    addBasicConstraintsExtension(builder, value);
                    break;
                case "subjectKeyIdentifier":
                    addSubjectKeyIdentifierExtension(builder, subjectPublicKey);
                    break;
                case "authorityKeyIdentifier":
                    addAuthorityKeyIdentifierExtension(builder, issuerPublicKey);
                    break;
                case "keyUsage":
                    addKeyUsageExtensions(builder, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void addBasicConstraintsExtension(X509v3CertificateBuilder certBuilder, String value) throws IOException {
        boolean boolValue = Boolean.parseBoolean(value.toLowerCase());
        certBuilder.addExtension(
                Extension.basicConstraints,
                true,
                new BasicConstraints(boolValue)
        );
    }

    private void addKeyUsageExtensions(X509v3CertificateBuilder certBuilder,String value) throws IOException {
        int extensions = 0;
        for (String c : value.split(",")) {
           extensions = extensions |Integer.valueOf(c.trim());
        }
        certBuilder.addExtension(
                Extension.keyUsage,
                true,
                new KeyUsage(extensions)
        );
    }

    private void addSubjectKeyIdentifierExtension(X509v3CertificateBuilder certBuilder, PublicKey publicKey) throws IOException {
        SubjectKeyIdentifier subjectKeyIdentifier = createSubjectKeyIdentifier(publicKey);
        certBuilder.addExtension(
                Extension.subjectKeyIdentifier,
                false,
                subjectKeyIdentifier
        );
    }

    private void addAuthorityKeyIdentifierExtension(X509v3CertificateBuilder certBuilder, PublicKey publicKey) throws IOException, NoSuchAlgorithmException {
        AuthorityKeyIdentifier authorityKeyIdentifier = createAuthorityKeyIdentifier(publicKey);
        certBuilder.addExtension(
                Extension.authorityKeyIdentifier,
                false,
                authorityKeyIdentifier
        );
    }

    private SubjectKeyIdentifier createSubjectKeyIdentifier(PublicKey publicKey) throws IOException {
        ASN1InputStream asn1InputStream = new ASN1InputStream(publicKey.getEncoded());
        ASN1Sequence publicKeyInfoSequence = DERSequence.getInstance(asn1InputStream.readObject());
        asn1InputStream.close();
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKeyInfoSequence);
        byte[] subjectKeyIdentifier = new DEROctetString(subjectPublicKeyInfo.getPublicKeyData().getBytes()).getEncoded();
        return new SubjectKeyIdentifier(subjectKeyIdentifier);
    }

    private AuthorityKeyIdentifier createAuthorityKeyIdentifier(PublicKey publicKey) throws IOException, NoSuchAlgorithmException {
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        return extensionUtils.createAuthorityKeyIdentifier(publicKey);
    }

}
