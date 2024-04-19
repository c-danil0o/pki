package com.security.pki;

import com.security.pki.repository.PrivateRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PkiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PkiApplication.class, args);
        PrivateRepository repository = new PrivateRepository();
    }


}
