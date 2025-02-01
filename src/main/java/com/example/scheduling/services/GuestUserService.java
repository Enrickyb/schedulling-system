package com.example.scheduling.services;

import com.example.scheduling.dto.GuestUserDTO;
import com.example.scheduling.models.GuestUser;
import com.example.scheduling.repositories.GuestUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GuestUserService {

    private final GuestUserRepository guestUserRepository;

    public GuestUserService(GuestUserRepository guestUserRepository) {
        this.guestUserRepository = guestUserRepository;
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

    public void deleteGuestUser(UUID userId) {
        guestUserRepository.deleteById(userId);
    }



}
