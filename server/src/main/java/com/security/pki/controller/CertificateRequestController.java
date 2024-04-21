package com.security.pki.controller;

import com.security.pki.model.Certificate;
import com.security.pki.model.Request;
import com.security.pki.service.CertificateGeneratorService;
import com.security.pki.service.CertificateRequestService;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/request")
public class CertificateRequestController {
    private final CertificateRequestService certificateRequestService;
    private final CertificateGeneratorService certificateGeneratorService;

    @Autowired
    public CertificateRequestController(CertificateRequestService certificateRequestService, CertificateGeneratorService certificateGeneratorService) {
        this.certificateRequestService = certificateRequestService;
        this.certificateGeneratorService = certificateGeneratorService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Boolean> createCertificateRequest(@RequestBody Request request){
        boolean result = certificateRequestService.SaveRequest(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAll(){
        return ResponseEntity.ok(this.certificateRequestService.getAll());
    }


    @CrossOrigin(origins = "http://localhost:4200")
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
