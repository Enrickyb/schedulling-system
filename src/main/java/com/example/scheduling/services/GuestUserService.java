package com.example.scheduling.services;

import com.example.scheduling.dto.GuestUserDTO;
import com.example.scheduling.models.Business;
import com.example.scheduling.models.GuestUser;
import com.example.scheduling.repositories.BusinessRepository;
import com.example.scheduling.repositories.GuestUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GuestUserService {

    private final GuestUserRepository guestUserRepository;
    private final BusinessRepository businessRepository;

    public GuestUserService(GuestUserRepository guestUserRepository, BusinessRepository businessRepository) {
        this.guestUserRepository = guestUserRepository;
        this.businessRepository = businessRepository;
    }

    public GuestUser registerGuestUser(GuestUser guestUser) {
        return guestUserRepository.save(guestUser);
    }

    public GuestUser getGuestUserByEmail(String email) {
        return guestUserRepository.findByEmail(email).orElse(null);
    }

    public GuestUser getGuestUserById(UUID userId) {
        return guestUserRepository.findById(userId).orElse(null);
    }

    public List<GuestUser> getAllGuestUsers() {
        return guestUserRepository.findAll();
    }

    public String deleteGuestUser(UUID userId) {
        guestUserRepository.deleteById(userId);
        return "Guest User deleted successfully";
    }

    public GuestUser updateGuestUser(UUID userId, GuestUserDTO guestUserDTO) {
        GuestUser guestUser = guestUserRepository.findById(userId).orElse(null);

        if (guestUser == null) {
            return null;
        }

        guestUser.setName(guestUserDTO.name());
        guestUser.setEmail(guestUserDTO.email());
        guestUser.setPhone(guestUserDTO.phone());
        guestUser.setAddress(guestUserDTO.address());
        guestUser.setCpf(guestUserDTO.cpf());

        return guestUserRepository.save(guestUser);
    }

    public Optional<GuestUser> getGuestUserByBusiness(UUID businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));

        return guestUserRepository.findByCreatedBy(business);
    }





}
