package com.security.pki.controller;

import com.security.pki.model.Certificate;
import com.security.pki.service.CertificateRevocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/certificate")
public class CertificateRevocationController {
    private final CertificateRevocationService revocationService;

    @Autowired
    public CertificateRevocationController(CertificateRevocationService revocationService) {
        this.revocationService = revocationService;
    }

    @PatchMapping(value = "/revoke/{serialNumber}")
    public ResponseEntity<Void> revokeCertificate(@PathVariable String serialNumber){
        this.revocationService.revokeCertificate(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
