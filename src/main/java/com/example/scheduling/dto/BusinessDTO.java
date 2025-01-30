package com.example.scheduling.dto;

import java.util.UUID;

public record BusinessDTO(
        UUID ownerId,
        String name,
        String description,
        String phone,
        String address
) {}
