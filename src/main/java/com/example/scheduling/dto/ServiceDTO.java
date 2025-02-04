package com.example.scheduling.dto;

import com.example.scheduling.models.Services;

import java.util.UUID;

public record ServiceDTO(
        UUID id,
        String name,
        String description,
        Double price,
        Integer duration,
        BusinessIdDTO business  // 🔹 Agora apenas uma empresa
) {
    public static ServiceDTO fromEntity(Services service) {
        return new ServiceDTO(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice(),
                service.getDuration(),
                BusinessIdDTO.fromEntity(service.getBusiness())  // 🔹 Obtém a empresa do serviço
        );
    }
}
