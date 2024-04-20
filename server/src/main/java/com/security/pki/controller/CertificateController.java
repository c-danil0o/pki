package com.security.pki.controller;

import com.security.pki.dto.CertificateDto;
import com.security.pki.dto.CertificateNodeDto;
import com.security.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificate")
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/all")
    public ResponseEntity<List<CertificateNodeDto>> getAllCertificateNodes(){
       return ResponseEntity.ok(this.certificateService.getAllCertificateNodes());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{serialNumber}")
    public ResponseEntity<CertificateDto> getBySerialNumber(@PathVariable String serialNumber){
        return ResponseEntity.ok(this.certificateService.getBySerialNumber(serialNumber));
    }
    
}
