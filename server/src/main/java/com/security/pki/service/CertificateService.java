package com.security.pki.service;

import com.security.pki.dto.CertificateDto;
import com.security.pki.dto.CertificateNodeDto;
import com.security.pki.dto.SignedCertificateDto;
import com.security.pki.exceptions.CertificateNotApprovedException;
import com.security.pki.exceptions.RequestNotFoundException;
import com.security.pki.model.Certificate;
import com.security.pki.model.CertificateStatus;
import com.security.pki.model.CertificateType;
import com.security.pki.model.certificateData.Issuer;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.KeyStoreRepository;
import com.security.pki.repository.PrivateRepository;
import com.security.pki.repository.RequestRepository;
import com.security.pki.util.CertificateBuilderUtils;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
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

    public SignedCertificateDto getCertificatePem(String alias){
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
            PrivateKey key = privateRepository.getKey("bookingCA");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE,key );
            
            SignedCertificateDto signedCertificateDto = new SignedCertificateDto();
            signedCertificateDto.setPemCertificate(pemCertificate);
            signedCertificateDto.setDigitalSignature(cipher.doFinal(this.hashSHA256(pemCertificate).getBytes(StandardCharsets.UTF_8)));
            
            return signedCertificateDto;
            
            
            
        } catch (CertificateEncodingException | IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    private String hashSHA256(String input){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] hash = md.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

}
