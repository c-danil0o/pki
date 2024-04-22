package com.security.pki.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignedCertificateDto {
    private String pemCertificate;
    private byte[] digitalSignature;
}
