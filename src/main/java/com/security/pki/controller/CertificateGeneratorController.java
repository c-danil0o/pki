package com.security.pki.controller;

import com.security.pki.dto.CertificateDto;
import com.security.pki.model.Certificate2;
import com.security.pki.model.User;
import com.security.pki.service.CertificateGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;

@RestController
@RequestMapping("/cert")
public class CertificateGeneratorController {
    private final CertificateGeneratorService certificateGeneratorService;
    public CertificateGeneratorController(CertificateGeneratorService certificateGeneratorService){

        this.certificateGeneratorService = certificateGeneratorService;
    }
//    public ResponseEntity<CertificateDto> getNewCert(@RequestBody User user){
//        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
//        Certificate2 certificate = certificateGeneratorService.getNewCertificate(user, keyPair.getPublic(), "alias123");
//        return new ResponseEntity<>(new CertificateDto(certificate));
//    }
    
    
}
