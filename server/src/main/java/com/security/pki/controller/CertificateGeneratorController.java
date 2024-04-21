package com.security.pki.controller;

import com.security.pki.model.Certificate;
import com.security.pki.model.Request;
import com.security.pki.service.CertificateGeneratorService;
import com.security.pki.service.CertificateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "generate")
public class CertificateGeneratorController {
    private final CertificateGeneratorService certificateGeneratorService;
    private final CertificateRequestService certificateRequestService;


    @Autowired
    public CertificateGeneratorController(CertificateGeneratorService certificateGeneratorService, CertificateRequestService certificateRequestService) {
        this.certificateGeneratorService = certificateGeneratorService;
        this.certificateRequestService = certificateRequestService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(consumes = "application/json", path = "/get")
    public ResponseEntity<Certificate> generateCertificate(@RequestBody Request request) {
        return (ResponseEntity.ok(this.certificateGeneratorService.get(request)));
    }



}
