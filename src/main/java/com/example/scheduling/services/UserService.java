package com.example.scheduling.services;


import com.example.scheduling.models.User;
import com.example.scheduling.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { // Construtor manual
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

}
