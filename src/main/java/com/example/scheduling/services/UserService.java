package com.example.scheduling.services;


import com.example.scheduling.dto.UserDTO;
import com.example.scheduling.dto.UserProjection;
import com.example.scheduling.models.User;
import com.example.scheduling.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<UserDTO> getUserByBusinessId(UUID businessId) {
        List<UserProjection> projections = userRepository.findByBusinessId(businessId);
        return projections.stream()
                .map(projection -> new UserDTO(
                        projection.getEmail(),
                        projection.getName(),
                        projection.getPhone(),
                        projection.getAddress()
                ))
                .collect(Collectors.toList());

    }


}
