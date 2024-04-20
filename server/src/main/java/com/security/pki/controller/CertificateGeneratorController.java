package com.security.pki.controller;

import com.security.pki.model.Certificate;
import com.security.pki.model.Request;
import com.security.pki.service.CertificateGeneratorService;
import com.security.pki.service.CertificateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "certificate")
public class CertificateGeneratorController {
    private final CertificateGeneratorService certificateGeneratorService;

    @Autowired
    public CertificateGeneratorController(CertificateGeneratorService certificateGeneratorService) {
        this.certificateGeneratorService = certificateGeneratorService;
    }

    @PostMapping(consumes = "application/json", path = "/get")
    public ResponseEntity<Certificate> generateCertificate(@RequestBody Request request) {
        return (ResponseEntity.ok(this.certificateGeneratorService.get(request)));
    }

    @PostMapping(consumes = "application/json", path = "/request")
    public ResponseEntity<Certificate> requestCertificate(@RequestBody Request request) {
        return (ResponseEntity.ok(this.certificateGeneratorService.get(request)));
    }

}
