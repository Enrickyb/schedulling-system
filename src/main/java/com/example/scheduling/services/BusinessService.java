package com.example.scheduling.services;

import com.example.scheduling.exceptions.BusinessNotFoundException;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.User;
import com.example.scheduling.repositories.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final UserService userService;
    private final BusinessSettingsService businessSettingsService;



    public BusinessService(BusinessRepository businessRepository, UserService userService, BusinessSettingsService businessSettingsService) { // Construtor manual
        this.businessRepository = businessRepository;
        this.userService = userService;
        this.businessSettingsService = businessSettingsService;
    }

    public Business createBusiness(Business business) {
        Business savedBusiness = businessRepository.save(business);
        businessSettingsService.createDefaultSettings(savedBusiness);
        return savedBusiness;
    }



    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    public Business getBusinessById(UUID businessId) {
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Negócio com ID " + businessId + " não encontrado."));
    }

    public List<Business> getBusinessesByOwner(UUID ownerId) {

        return businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new BusinessNotFoundException("Negócio com ID do dono " + ownerId + " não encontrado."));
    }


}
