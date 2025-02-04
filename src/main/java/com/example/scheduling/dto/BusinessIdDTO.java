package com.example.scheduling.dto;

import java.util.UUID;

import com.example.scheduling.models.Business;


public record BusinessIdDTO(UUID id) {
    public static BusinessIdDTO fromEntity(Business business) {
        return new BusinessIdDTO(business.getId());
    }
}
