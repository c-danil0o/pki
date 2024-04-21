package com.security.pki.controller;

import com.security.pki.service.CertificateDeletionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "certificate")
public class CertificateDeletionController {
    private final CertificateDeletionService certificateDeletionService;

    public CertificateDeletionController(CertificateDeletionService certificateDeletionService) {
        this.certificateDeletionService = certificateDeletionService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(value = "/{serialNumber}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable String serialNumber){
        certificateDeletionService.deleteCertificate(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
