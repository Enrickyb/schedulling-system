package com.example.scheduling.services;

import com.example.scheduling.models.Business;
import com.example.scheduling.repositories.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) { // Construtor manual
        this.businessRepository = businessRepository;
    }

    public Business createBusiness(Business business) {
        return businessRepository.save(business);
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    public Optional<Business> getBusinessById(UUID businessId) {
        return businessRepository.findById(businessId);
    }
}
