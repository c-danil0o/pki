package com.security.pki.controller;

import com.security.pki.dto.CertificateDto;
import com.security.pki.model.Certificate;
import com.security.pki.model.Request;
import com.security.pki.model.User;
import com.security.pki.service.CertificateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "cert")
public class CertificateGeneratorController {
    private final CertificateGeneratorService certificateGeneratorService;

    @Autowired
    public CertificateGeneratorController(CertificateGeneratorService certificateGeneratorService){
        this.certificateGeneratorService = certificateGeneratorService;
    }

    @GetMapping(value = "/root")
    public ResponseEntity<Certificate> generateRootCertificate(){
        Certificate certificate = this.certificateGeneratorService.generateRootCertificate();
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping(value = "/intermediate")
    public ResponseEntity<Certificate> generateIntermediateCertificate(@RequestBody User user,
                                                                       @RequestParam LocalDate validTo, @RequestParam String alias){
        Certificate certificate = this.certificateGeneratorService.generateIntermediateCertificate(user, validTo, alias);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    

//    @PostMapping(consumes = "application/json")
//    public ResponseEntity<CertificateDto> genereteCertificate(@RequestParam Long requestId){
//
//    }
    
    
}
