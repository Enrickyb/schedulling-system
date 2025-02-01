package com.example.scheduling.dto;

import java.util.UUID;

public record GuestUserDTO(String email, String name, String cpf, String phone, String address, UUID created_by) {
}
