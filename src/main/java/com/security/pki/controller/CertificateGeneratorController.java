package com.security.pki.controller;

import com.security.pki.dto.CertificateDto;
import com.security.pki.model.Certificate;
import com.security.pki.model.Request;
import com.security.pki.service.CertificateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cert")
public class CertificateGeneratorController {
    private final CertificateGeneratorService certificateGeneratorService;

    @Autowired
    public CertificateGeneratorController(CertificateGeneratorService certificateGeneratorService){
        this.certificateGeneratorService = certificateGeneratorService;
    }

//    @PostMapping(consumes = "application/json")
//    public ResponseEntity<CertificateDto> GenereteCertificate(@RequestParam Long requestId){
//
//    }
    
    
}
