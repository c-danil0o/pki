package com.security.pki;

import com.security.pki.repository.PrivateRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class PkiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PkiApplication.class, args);
    }
}
