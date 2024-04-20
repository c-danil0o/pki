package com.security.pki.repository;

import com.security.pki.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, String> {
    List<Certificate> findCertificateByIssuerSerialNumber(String serialNumber);
}
