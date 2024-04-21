package com.security.pki.controller;

import com.security.pki.dto.CertificateDto;
import com.security.pki.dto.CertificateNodeDto;
import com.security.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/all")
    public ResponseEntity<List<CertificateNodeDto>> getAllCertificateNodes(){
       return ResponseEntity.ok(this.certificateService.getAllCertificateNodes());
    }


    @GetMapping("/{serialNumber}")
    public ResponseEntity<CertificateDto> getBySerialNumber(@PathVariable String serialNumber){
        return ResponseEntity.ok(this.certificateService.getBySerialNumber(serialNumber));
    }

    @GetMapping("/validate/{serialNumber}")
    public ResponseEntity<Boolean> isValid(@PathVariable String serialNumber){
        return ResponseEntity.ok(this.certificateService.isValid(serialNumber));
    }

    @GetMapping("/alias/{alias}")
    public ResponseEntity<CertificateDto> getByAlias(@PathVariable String alias){
        CertificateDto certificateDto = this.certificateService.getByAlias(alias);
        if(certificateDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(this.certificateService.getByAlias(alias));
    }

    @GetMapping("/PEM/{alias}")
    public ResponseEntity<String> getPemByAlias(@PathVariable String alias){
        return ResponseEntity.ok(this.certificateService.getCertificatePem(alias));
    }
    
}
