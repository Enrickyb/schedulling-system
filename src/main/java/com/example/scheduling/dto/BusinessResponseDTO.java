package com.example.scheduling.dto;

import com.example.scheduling.models.Business;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record BusinessResponseDTO(
        UUID id,
        String name,
        String description,
        String phone,
        String address,
        String cnpj,
        String email,
        Set<ServiceDTO> services
) {
    public static BusinessResponseDTO fromEntity(Business business) {
        return new BusinessResponseDTO(
                business.getId(),
                business.getName(),
                business.getDescription(),
                business.getPhone(),
                business.getAddress(),
                business.getCnpj(),
                business.getEmail(),
                business.getServices().stream()
                        .map(ServiceDTO::fromEntity)
                        .collect(Collectors.toSet())
        );
    }
}
