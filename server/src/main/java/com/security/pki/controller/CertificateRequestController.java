package com.security.pki.controller;

import com.security.pki.model.Request;
import com.security.pki.service.CertificateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class CertificateRequestController {
    private final CertificateRequestService certificateRequestService;

    @Autowired
    public CertificateRequestController(CertificateRequestService certificateRequestService) {
        this.certificateRequestService = certificateRequestService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Boolean> createCertificateRequest(@RequestBody Request request){
        boolean result = certificateRequestService.SaveRequest(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
