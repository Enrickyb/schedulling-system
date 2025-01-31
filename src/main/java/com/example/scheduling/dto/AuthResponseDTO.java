package com.example.scheduling.dto;

import com.example.scheduling.enums.UserRole;

import java.util.UUID;

public record AuthResponseDTO(String token, UUID user_id, String user_name, String user_email, UserRole user_role, String user_address, String user_phone) {}
