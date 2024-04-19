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

    public void addIntermediateExtensions(X509v3CertificateBuilder certBuilder, PublicKey publicKey, ContentSigner contentSigner) {
        try {
            addBasicConstraintsExtension(certBuilder);
            addKeyUsageExtension(certBuilder);
            addSubjectKeyIdentifierExtension(certBuilder, publicKey);
            addAuthorityKeyIdentifierExtension(certBuilder, publicKey);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error adding intermediate extensions", e);
        }
    }

    private void addBasicConstraintsExtension(X509v3CertificateBuilder certBuilder) throws IOException {
        certBuilder.addExtension(
                Extension.basicConstraints,
                true,
                new BasicConstraints(true)
        );
    }

    private void addKeyUsageExtension(X509v3CertificateBuilder certBuilder) throws IOException {
        certBuilder.addExtension(
                Extension.keyUsage,
                true,
                new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
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
