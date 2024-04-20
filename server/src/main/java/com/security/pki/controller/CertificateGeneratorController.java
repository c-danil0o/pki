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
@RequestMapping(value = "/generate")
public class CertificateGeneratorController {
    private final CertificateGeneratorService certificateGeneratorService;
    private final CertificateRequestService certificateRequestService;


    @Autowired
    public CertificateGeneratorController(CertificateGeneratorService certificateGeneratorService, CertificateRequestService certificateRequestService) {
        this.certificateGeneratorService = certificateGeneratorService;
        this.certificateRequestService = certificateRequestService;
    }

    @PostMapping(consumes = "application/json", path = "/get")
    public ResponseEntity<Certificate> generateCertificate(@RequestBody Request request) {
        return (ResponseEntity.ok(this.certificateGeneratorService.get(request)));
    }

    @PostMapping(consumes = "application/json", path = "/request")
    public ResponseEntity<Boolean> requestCertificate(@RequestBody Request request) {
        boolean result = certificateRequestService.SaveRequest(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/accept")
    public ResponseEntity<Certificate> acceptRequest(@RequestParam Long requestId){
        Request request = certificateRequestService.findById(requestId);
        if (request!=null){
            Certificate certificate = this.certificateGeneratorService.get(request);
            this.certificateRequestService.deleteById(requestId);
            return (ResponseEntity.ok(certificate));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
