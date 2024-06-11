package com.security.pki.service;

import com.security.pki.exceptions.AliasAlreadyExistsException;
import com.security.pki.model.Request;
import com.security.pki.repository.CertificateRepository;
import com.security.pki.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateRequestService {
    private final RequestRepository requestRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateRequestService(RequestRepository requestRepository, CertificateRepository certificateRepository) {
        this.requestRepository = requestRepository;
        this.certificateRepository = certificateRepository;
    }

    public boolean SaveRequest(Request request){
        if (requestRepository.findRequestByAlias(request.getAlias())!=null || certificateRepository.findCertificateByAlias(request.getAlias()) != null){
            throw new AliasAlreadyExistsException("You already made request");
        }
        try {
            this.requestRepository.save(request);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public List<Request> getAll(){
        return this.requestRepository.findAll();
    }

    public Request findById(Long id){
        return requestRepository.findById(id).orElseGet(null);
    }

    public void deleteById(Long id){
        this.requestRepository.deleteById(id);
    }



}
