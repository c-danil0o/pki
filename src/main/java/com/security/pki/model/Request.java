package com.security.pki.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
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
    private LocalDate validTo;
    @Column
    private String publicKey;
}
