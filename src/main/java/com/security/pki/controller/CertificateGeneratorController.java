package com.security.pki.controller;

import com.security.pki.service.CertificateGeneratorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
