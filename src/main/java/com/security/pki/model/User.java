package com.security.pki.model;

import lombok.Data;

@Data
public class User {
    private Long accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String organisation;
    private String countryCode;
    private String alias;
    private String publicKey;
}

