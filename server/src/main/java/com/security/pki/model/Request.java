package com.security.pki.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    @Column
    private Long accountId;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String organisation;
    @Column
    private String countryCode;
    @Column
    private String alias;
    @Column
    private String signerAlias;
    @Column
    @Temporal(TemporalType.DATE)
    private Date validTo;
    @Column(nullable = true)
    private String publicKey;
    @Column
    private CertificateType type;
    @ElementCollection
    private HashMap<String, String> extensions;
}
