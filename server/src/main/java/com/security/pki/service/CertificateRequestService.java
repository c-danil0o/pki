package com.security.pki.service;

import com.security.pki.model.Request;
import com.security.pki.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateRequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public CertificateRequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public boolean SaveRequest(Request request){
        try {
            this.requestRepository.save(request);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public Request findById(Long id){
        return requestRepository.findById(id).orElseGet(null);
    }

    public void deleteById(Long id){
        this.requestRepository.deleteById(id);
    }



}
