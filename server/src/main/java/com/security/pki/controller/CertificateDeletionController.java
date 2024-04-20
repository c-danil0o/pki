package com.security.pki.controller;

import com.security.pki.service.CertificateDeletionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "certificate")
public class CertificateDeletionController {
    private final CertificateDeletionService certificateDeletionService;

    public CertificateDeletionController(CertificateDeletionService certificateDeletionService) {
        this.certificateDeletionService = certificateDeletionService;
    }

    @DeleteMapping(value = "/{serialNumber}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable String serialNumber){
        certificateDeletionService.deleteCertificate(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
